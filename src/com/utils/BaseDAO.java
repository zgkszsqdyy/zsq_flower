package com.utils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO {
    //对一张表中只查一行数据的通用查询(终极版：对所有有封装类的表都有效)2.0(用于事务的查找，取消conn的自动关闭)
    public <T> T selectAll(Connection conn, Class<T> clazz, String sql, Object...args){
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            // 2.预编译sql语句，得到PreparedStatement对象
            ps = conn.prepareStatement(sql);
            // 3.填充占位符
            for(int i = 0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            // 4.执行executeQuery(),得到结果集：ResultSet
            resultSet = ps.executeQuery();
            // 5.得到结果集的元数据：ResultSetMetaData
            ResultSetMetaData metaData = resultSet.getMetaData();
            // 6.1通过ResultSetMetaData结果集的列数
            int col = metaData.getColumnCount();
            if (resultSet.next()){
                T t = clazz.newInstance();
                for (int i = 0;i<col;i++){// 遍历每一个列
                    // 获取列值:通过结果集：ResultSet
                    Object Value = resultSet.getObject(i + 1);
                    // 通过ResultSetMetaData
                    //获取列的列名：getColumnName();-->不推荐使用，因为当列名和类属性名不一致时会报错
                    //获取列的别名（当列名和属性名不一致时用来顶替原来的列名，别名和属性名相同）
                    //-- > getColumnLabel();----->当没有别名时，获取的就是列名
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    // 6.2使用反射，给对象的相应属性赋值
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,Value);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.closeResourse(null,ps,resultSet);
        }
        return null;
    }
    //对一张表中查多行数据的通用查询(终极版：对所有有封装类的表都有效）2.0(用于事务的查找，取消conn的自动关闭)
    public <T> List<T> selectAlls(Connection conn, Class<T> clazz, String sql, Object...args){
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            // 2.预编译sql语句，得到PreparedStatement对象
            ps = conn.prepareStatement(sql);
            // 3.填充占位符
            for(int i = 0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            // 4.执行executeQuery(),得到结果集：ResultSet
            resultSet = ps.executeQuery();
            // 5.得到结果集的元数据：ResultSetMetaData
            ResultSetMetaData metaData = resultSet.getMetaData();
            // 6.1通过ResultSetMetaData结果集的列数
            int col = metaData.getColumnCount();
            //创建集合来储存数据对象
            ArrayList<T> list = new ArrayList<>();
            while (resultSet.next()){
                T t = clazz.newInstance();
                //处理结果集一行数据的每一个列：给t对象指定的属性赋值
                for (int i = 0;i<col;i++){// 遍历每一个列
                    // 获取列值:通过结果集：ResultSet
                    Object Value = resultSet.getObject(i + 1);
                    // 通过ResultSetMetaData
                    //获取列的列名：getColumnName();-->不推荐使用，因为当列名和类属性名不一致时会报错
                    //获取列的别名（当列名和属性名不一致时用来顶替原来的列名，别名和属性名相同）
                    //-- > getColumnLabel();----->当没有别名时，获取的就是列名
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    // 6.2使用反射，给对象的相应属性赋值
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,Value);
                }
                list.add(t);
            }
                return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.closeResourse(null,ps,resultSet);
        }
        return null;
    }
    //修改的通用方法2.0(用于事务的修改，取消conn的自动关闭)
    public int updateAll(Connection conn,String sql,Object... args){
        PreparedStatement ps = null;
        try {
            //预编译SQL语句，得到prepareStatement实例
            ps = conn.prepareStatement(sql);
            //填充占位符
            for (int i = 0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            //执行
            //ps.execute()：执行的时查找操作时(有结果集)，返回true；增删改操作返回的是false;
            //ps.execute();
            return ps.executeUpdate();//返回的影响表的行数，一般用来判断操作是否执行成功
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.closeResourse(null,ps);
        }
        return 0;
    }
    //用于查询特殊值的方法（max,min...）,针对于一行一列的数据
    public <T> T getValue(Connection conn,String sql,Object...args){
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            ps = conn.prepareStatement(sql);
            for(int i = 0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            resultSet = ps.executeQuery();
            if(resultSet.next()){
                return (T) resultSet.getObject(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JdbcUtil.closeResourse(null,ps,resultSet);
        }
        return null;
    }
}

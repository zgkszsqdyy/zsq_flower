package com.utils;


import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JdbcUtil {

    private static DataSource source;
    static {
        try {
            Properties pres = new Properties();
            InputStream is = JdbcUtil.class.getClassLoader().getResourceAsStream("druid.properties");
            pres.load(is);
            source = DruidDataSourceFactory.createDataSource(pres);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static Connection getConnection(){
        Connection conn = null;
        try {
            conn = source.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }
//    public static Connection getConnection(){
//        Properties pro = new Properties();
//        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("  druid.properties");
//        Connection conn = null;
//        try {
//            pro.load(is);
//            DataSource source = DruidDataSourceFactory.createDataSource(pro);
//            conn = source.getConnection();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return conn;
//    }
    @Test
    public void sc(){
        Connection conn = JdbcUtil.getConnection();
        System.out.println(conn);
    }
//    //获取数据库的连接
//    public static Connection getConnection(){
//        Connection conn = null;
//        try {
//            String url = "jdbc:mysql://localhost:3306/book";
//            String user = "root";
//            String password = "200035qwe";
//            String driverName = "com.mysql.jdbc.Driver";
//            //3.加载驱动
//            Class.forName(driverName);
//            //4.获取连接
//            conn = DriverManager.getConnection(url, user, password);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        return conn;
//    }
    //关闭连接和Statment操作
    public static void closeResourse(Connection conn,Statement ps){
        try {
            if(ps!=null)
                ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (conn!=null)
                conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    //关闭连接和Statment操作和ResultSet
    public static void closeResourse(Connection conn,Statement ps,ResultSet re){
        try {
            if(ps!=null)
                ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (conn!=null)
                conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (re!=null)
                re.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    //对一张表中只查一行数据的通用查询(终极版：对所有有封装类的表都有效)
    public static  <T> T selectAll(Class<T> clazz,String sql,Object...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            // 1.获取数据库连接
            conn = JdbcUtil.getConnection();
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
            JdbcUtil.closeResourse(conn,ps,resultSet);
        }
        return null;
    }

    //对一张表中只查一行数据的通用查询(终极版：对所有有封装类的表都有效)2.0(用于事务的查找，取消conn的自动关闭)
    public static  <T> T selectAll(Connection conn,Class<T> clazz,String sql,Object...args){
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            // 1.获取数据库连接
            conn = JdbcUtil.getConnection();
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
    //对一张表中查多行数据的通用查询(终极版：对所有有封装类的表都有效）
    public static <T> List<T> selectAlls(Class<T> clazz, String sql, Object...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            // 1.获取数据库连接
            conn = JdbcUtil.getConnection();
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
            JdbcUtil.closeResourse(conn,ps,resultSet);
        }
        return null;
    }
    //对一张表中查多行数据的通用查询(终极版：对所有有封装类的表都有效）2.0(用于事务的查找，取消conn的自动关闭)
    public static <T> List<T> selectAlls(Connection conn,Class<T> clazz, String sql, Object...args){
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            // 1.获取数据库连接
            conn = JdbcUtil.getConnection();
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
    //修改的通用方法
    public static int updateAll(String sql,Object... args){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //获取数据库连接
            conn = JdbcUtil.getConnection();
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
            JdbcUtil.closeResourse(conn,ps);
        }
        return 0;
    }

    //修改的通用方法2.0(用于事务的修改，取消conn的自动关闭)
    public static int updateAll(Connection conn,String sql,Object... args){
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
    //用于查询特殊值的方法（max,min...）
    public static <T> T getValue(Connection conn,String sql,Object...args){
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

package com.hr.Servlet;

import com.hr.dao.*;
import com.hr.entity.*;
import com.hr.util.EncodeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

public class MingxiServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EncodeUtil.encode(req);
        resp.setContentType("text/html;charset=utf-8");
        //查询分类数据
        ArrayList<EASYBUY_PRODUCT_CATEGORY> flist = EASYBUY_PRODUCT_CATEGORYDao.selectFather();
        req.setAttribute("flist", flist);
        ArrayList<EASYBUY_PRODUCT_CATEGORY> clist = EASYBUY_PRODUCT_CATEGORYDao.selectChild();
        req.setAttribute("clist", clist);
        ArrayList<EASYBUY_PRODUCT> tlist = EASYBUY_PRODUCTDao.selectAllByT();
        req.setAttribute("tlist", tlist);
        ArrayList<EASYBUY_PRODUCT> hlist = EASYBUY_PRODUCTDao.selectAllByHot();
        req.setAttribute("hlist", hlist);
        ArrayList<EASYBUY_NEWS> nlist = EASYBUY_NEWSDao.selectAll();
        req.setAttribute("nlist", nlist);
        HttpSession session = req.getSession();
        //查询最近浏览的商品
        ArrayList<Integer> ids = (ArrayList<Integer>)session.getAttribute("ids");
        if(ids!=null){
            ArrayList<EASYBUY_PRODUCT> lastlylist = EASYBUY_PRODUCTDao.selectById(ids);
            req.setAttribute("lastlylist", lastlylist);
        }
        int id = Integer.parseInt(req.getParameter("id"));
        //System.out.println(id);
        ArrayList<EASYBUY_ORDER> order = EASYBUY_DdanDao.selectById(id);
        //System.out.println(order);
        req.setAttribute("order",order);
        req.getRequestDispatcher("/DanMingXi.jsp").forward(req,resp);
    }
}

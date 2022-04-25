package com.web.shopservlet;


import com.hr.dao.ESDao;
import com.hr.entity.EASYBUY_ORDER_DETAIL;
import com.hr.entity.EASYBUY_USER;
import com.hr.util.EncodeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;


public class gmServlet extends HttpServlet {
@Override
protected void service(HttpServletRequest arg0, HttpServletResponse arg1)
		throws ServletException, IOException {
	arg1.setContentType("text/html;charset=utf-8");
	PrintWriter out=arg1.getWriter();
	EncodeUtil.encode(arg0);
	HttpSession session=arg0.getSession();
	EASYBUY_USER list=(EASYBUY_USER)session.getAttribute("name");
	//�û�id
	String id=list.getEU_USER_ID();
	//�û�����
	String name=list.getEU_USER_NAME();
	//�û���ַ
	String address=list.getEU_ADDRESS();
	//�ܼ�Ǯ
	String price=arg0.getParameter("jstext");
	////////////////
	//���ﳵid
	String [] EP_ID=arg0.getParameterValues("op");
	//��Ʒid
	int [] es_ep_id = new int[EP_ID.length];
	for (int i = 0 ; i<EP_ID.length ; i++){
		es_ep_id[i] = ESDao.getEs_ep_id(Integer.parseInt(EP_ID[i])).es_ep_id;
	}
	//��������
	String [] quantity=arg0.getParameterValues("number");
	//��Ʒ����
	String [] sPPrice=arg0.getParameterValues("sPPrice");
	
	//��������Ʒ��Ŀ������޸�
	for(int i=0;i<es_ep_id.length;i++){
		int count5=ESDao.updateStock(Integer.parseInt(quantity[i]),es_ep_id[i]);
	}
	//��Ʒ�����ܼ�
	int [] pprice=new int[es_ep_id.length];
	for(int i=0;i<es_ep_id.length;i++){
		pprice[i]=Integer.parseInt(quantity[i])*Integer.parseInt(sPPrice[i]);
	}
	int oid = (int) (System.currentTimeMillis()+Math.round((Math.random()+1) * 1000)+new Random().nextInt(9999));
	/////////////////�õ�����
	//�����������������
	int count=ESDao.insertDD(oid,id, name, address,Integer.parseInt(price));
	int getSequenceId=ESDao.getSequenceId();
	//ѭ���������������
	for(int i=0;i<es_ep_id.length;i++){
		EASYBUY_ORDER_DETAIL eod=new EASYBUY_ORDER_DETAIL(oid,getSequenceId,es_ep_id[i],Integer.parseInt(quantity[i]),pprice[i]);
		int count2=ESDao.eodInsert(eod);
	}
	
	
	////////////////////
	// �������޸Ĺ��ﳵ
	String [] esID=arg0.getParameterValues("op");
	for(int i=0;i<esID.length;i++){
		int count3 =ESDao.esdelete(Integer.parseInt(esID[i]));
	}
	/////
	if(count>0){
			
		out.print("<script>");
		out.print("alert('����ɹ�');");
		out.print("location.href='shopping-result.jsp';");
		out.print("</script>");
		out.close();
	}else{
		out.print("<script>");
		out.print("alert('����ʧ�ܣ�������ѡ����Ʒ');");
		out.print("location.href='ShopSelect';");
		out.print("</script>");
		out.close();
	}
}
}

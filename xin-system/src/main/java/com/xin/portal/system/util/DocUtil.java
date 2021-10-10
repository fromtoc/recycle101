package com.xin.portal.system.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;

/**
 * Created by Administrator
 * on 2018/3/12
 */
public class DocUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(DocUtil.class);
	
    private static DocUtil doc2HtmlUtil;

    /**
     * 获取Doc2HtmlUtil实例
     */
    public static synchronized DocUtil getDoc2HtmlUtilInstance() {
        if (doc2HtmlUtil == null) {
            doc2HtmlUtil = new DocUtil();
        }
        return doc2HtmlUtil;
    }

    /**
     * 转换文件成html
     *
     * @param fromFileInputStream:
     * @throws IOException
     */
    public String file2Html(InputStream fromFileInputStream, String toFilePath,String type,String host,int port) throws IOException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timesuffix = sdf.format(date);
        String docFileName = null;
        String htmFileName = null;
        if("doc".equals(type)){
            docFileName = "doc_" + timesuffix + ".doc";
            htmFileName = "doc_" + timesuffix + ".html";
        }else if("docx".equals(type)){
            docFileName = "docx_" + timesuffix + ".docx";
            htmFileName = "docx_" + timesuffix + ".html";
        }else if("xls".equals(type)){
            docFileName = "xls_" + timesuffix + ".xls";
            htmFileName = "xls_" + timesuffix + ".html";
        }else if("ppt".equals(type)){
            docFileName = "ppt_" + timesuffix + ".ppt";
            htmFileName = "ppt_" + timesuffix + ".html";
        }else{
            return null;
        }

        File htmlOutputFile = new File(toFilePath + File.separatorChar + htmFileName);
        File docInputFile = new File(toFilePath + File.separatorChar + docFileName);
        if (htmlOutputFile.exists())
            htmlOutputFile.delete();
        htmlOutputFile.createNewFile();
        if (docInputFile.exists())
            docInputFile.delete();
        docInputFile.createNewFile();
        /**
         * 由fromFileInputStream构建输入文件
         */
        try {
            OutputStream os = new FileOutputStream(docInputFile);
            int bytesRead = 0;
            byte[] buffer = new byte[1024 * 8];
            while ((bytesRead = fromFileInputStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            os.close();
            fromFileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        OpenOfficeConnection connection = new SocketOpenOfficeConnection(host,port);
        try {
            connection.connect();
        } catch (ConnectException e) {
            System.err.println("文件转换出错，请检查OpenOffice服务是否启动。");
        }
        // convert
        DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
        converter.convert(docInputFile, htmlOutputFile);
        connection.disconnect();
        // 转换完之后删除word文件
        docInputFile.delete();
        return htmFileName;
    }

    /**
     * 转换文件成pdf
     *
     * @param fromFileInputStream:
     * @throws IOException
     */
    public String file2pdf(InputStream fromFileInputStream, String toFilePath,String type,String host,int port) throws IOException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timesuffix = sdf.format(date);
        String docFileName = null;
        String htmFileName = null;
        if("doc".equals(type)){
            docFileName = "doc_" + timesuffix + ".doc";
            htmFileName = "doc_" + timesuffix + ".pdf";
        }else if("docx".equals(type)){
            docFileName = "docx_" + timesuffix + ".docx";
            htmFileName = "docx_" + timesuffix + ".pdf";
        }else if("xls".equals(type)){
            docFileName = "xls_" + timesuffix + ".xls";
            htmFileName = "xls_" + timesuffix + ".pdf";
        }else if("ppt".equals(type)){
            docFileName = "ppt_" + timesuffix + ".ppt";
            htmFileName = "ppt_" + timesuffix + ".pdf";
        }else{
            return null;
        }

        File htmlOutputFile = new File(toFilePath + File.separatorChar + htmFileName);
        File docInputFile = new File(toFilePath + File.separatorChar + docFileName);
        if (htmlOutputFile.exists())
            htmlOutputFile.delete();
        htmlOutputFile.createNewFile();
        if (docInputFile.exists())
            docInputFile.delete();
        docInputFile.createNewFile();
        /**
         * 由fromFileInputStream构建输入文件
         */
        try {
            OutputStream os = new FileOutputStream(docInputFile);
            int bytesRead = 0;
            byte[] buffer = new byte[1024 * 8];
            while ((bytesRead = fromFileInputStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            os.close();
            fromFileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        OpenOfficeConnection connection = new SocketOpenOfficeConnection(host,port);
        
        try {
            connection.connect();
        } catch (ConnectException e) {
            System.err.println("文件转换出错，请检查OpenOffice服务是否启动。");
        }
        // convert
//        DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
        DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);
        converter.convert(docInputFile, htmlOutputFile);
        connection.disconnect();
        // 转换完之后删除word文件
        docInputFile.delete();
        System.out.println("文件转换完成");
        return htmFileName;
    }


    /**
     * 执行前，请启动openoffice服务
     * 进入$OO_HOME\program下
     * 执行soffice -headless -accept="socket,host=127.0.0.1,port=8100;urp;"
     * @param xlsfile
     * @param targetfile
     * @throws Exception
     */
    public static void fileConvertPdf(String xlsfile, String targetfile,String type,String host,int port, boolean isLocal)
            throws Exception {
        File xlsf = new File(xlsfile);
        File targetF = new File(targetfile);
        // stream 流的形式
        InputStream inputStream = new FileInputStream(xlsf);
        OutputStream outputStream = new FileOutputStream(targetF);
        fileConvertPdf(inputStream, outputStream, host, host, port, isLocal);
    }
    
    public static void fileConvertPdf(File inFile, File outfile,String type,String host,int port, boolean isLocal)
            throws Exception {
        // stream 流的形式
        InputStream inputStream = new FileInputStream(inFile);
        OutputStream outputStream = new FileOutputStream(outfile);
        fileConvertPdf(inputStream, outputStream, host, host, port, isLocal);
    }

    /**
     * 执行前，请启动openoffice服务
     * 进入$OO_HOME\program下
     * 执行soffice -headless -accept="socket,host=127.0.0.1,port=8100;urp;"  或 soffice -headless -accept="socket,host=127.0.0.1,port=8100;urp;" -nofirststartwizard
     * @param inputStream
     * @param outputStream
     * @throws Exception
     */
    public static void fileConvertPdf(InputStream inputStream, OutputStream outputStream,String type,String host,int port, boolean isLocal)
            throws Exception {
        // 获得文件格式
        DefaultDocumentFormatRegistry formatReg = new DefaultDocumentFormatRegistry();
        DocumentFormat pdfFormat = formatReg.getFormatByFileExtension("pdf");
        DocumentFormat docFormat = null;
        if(".doc".equals(type) || ".docx".equals(type)){
            docFormat = formatReg.getFormatByFileExtension("doc");
        }else if(".xls".equals(type) || ".xlsx".equals(type)){
            docFormat = formatReg.getFormatByFileExtension("xls");
        }else if(".ppt".equals(type) || ".pptx".equals(type)){
            docFormat = formatReg.getFormatByFileExtension("ppt");
        }else if(".txt".equals(type)){
            docFormat = formatReg.getFormatByFileExtension("txt");
        }else if(".pdf".equals(type)){
            docFormat = formatReg.getFormatByFileExtension("pdf");
        }else{
            docFormat = formatReg.getFormatByFileExtension("txt");
        }
        // stream 流的形式
        OpenOfficeConnection connection = new SocketOpenOfficeConnection(host,port);
        try {

            connection.connect();
            DocumentConverter converter = null;
            if (isLocal) {
            	converter = new OpenOfficeDocumentConverter(connection);
            } else {
            	converter = new StreamOpenOfficeDocumentConverter(connection);
            }
            converter.convert(inputStream, docFormat, outputStream, pdfFormat);
        } catch (ConnectException e) {
        	logger.error("openoffice connection failed! {}:{}",host,port);
            e.printStackTrace();
        } catch (Exception e) {
        	logger.error("openoffice convert failed! {}",type);
            e.printStackTrace();
        } finally {
        	if (inputStream!=null) {
        		inputStream.close();
        	}
        	if (outputStream!=null) {
        		outputStream.close();
        	}
            if (connection != null) {
                connection.disconnect();
                connection = null;
            }
        }
    }


    public static void main(String[] args) throws Exception {

        /*URL url=new URL("http://192.168.6.152:9000/group1/M00/00/0A/wKgGmFqaFKKAb6ixAAAYAFWbzCU610.xls");//默认主页
        InputStream is=url.openStream();//获取网络流


        //获取网络资源,编码格式不同会出现乱码****************
//        byte[] flush=new byte[1024];
//        int len=0;
//        while(-1!=(len=is.read(flush)))
//        {
//
//            System.out.println(new String(flush,0,len));
//        }
//        is.close();
        //获取网络资源,编码格式不同会出现乱码****************


        //解决乱码的方法,转换流
        BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream(),"utf-8"));//解码方式,utf-8
        String msg=null;
        BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:\\poi-test\\1.xls"),"utf-8"));
        while((msg=br.readLine())!=null)
        {
            bw.append(msg);
            bw.newLine();
        }
        bw.flush();
        bw.close();
        br.close();*/




        DocUtil coc2HtmlUtil = getDoc2HtmlUtilInstance();
//        String a = "E:\\1.xlsx";
//        String b = "D:\\exportExcel.pdf";
//        InputStream inputStream = new FileInputStream(a);
//        OutputStream outputStream = new FileOutputStream(b);
//        coc2HtmlUtil.fileConvertPdf(inputStream,outputStream,"xlsx","192.168.1.175",8100);

    	FileInputStream fileInputStream = null;

//    	File file = new File("E:\\1.xlsx");
//    	File file = new File("C:\\Users\\dod123456\\Documents\\WeChat Files\\yiye379917\\Files\\MSTR overview_8i_台湾版.ppt");
//    	File file = new File("C:\\Users\\dod123456\\Documents\\WeChat Files\\yiye379917\\Files\\《营业部规范管理纲要》解读.pptx");
    	File file = new File("C:\\Users\\dod123456\\Documents\\WeChat Files\\yiye379917\\Files\\简历.docx");
//    	File file = new File("E:\\1.xlsx");
    	
        fileInputStream = new FileInputStream(file);
     // coc2HtmlUtil.file2Html(fileInputStream, "D:/poi-test/openOffice/xls","xls");
        coc2HtmlUtil.file2pdf(fileInputStream, "E:\\xls","docx","192.168.1.175",8100);
        
        
    	

      /*  File file = null;
        FileInputStream fileInputStream = null;

        file = new File("D:\\poi-test\\exportExcel.xls");
        fileInputStream = new FileInputStream(file);
     // coc2HtmlUtil.file2Html(fileInputStream, "D:/poi-test/openOffice/xls","xls");
        coc2HtmlUtil.file2pdf(fileInputStream, "D:\\poi-test\\openOffice\\xls","xls");*/

/*        file = new File("D:/poi-test/test.doc");
        fileInputStream = new FileInputStream(file);
//      coc2HtmlUtil.file2Html(fileInputStream, "D:/poi-test/openOffice/doc","doc");
        coc2HtmlUtil.file2pdf(fileInputStream, "D:/poi-test/openOffice/doc","doc");

        file = new File("D:/poi-test/周报模版.ppt");
        fileInputStream = new FileInputStream(file);
//      coc2HtmlUtil.file2Html(fileInputStream, "D:/poi-test/openOffice/ppt","ppt");
        coc2HtmlUtil.file2pdf(fileInputStream, "D:/poi-test/openOffice/ppt","ppt");

        file = new File("D:/poi-test/test.docx");
        fileInputStream = new FileInputStream(file);
//      coc2HtmlUtil.file2Html(fileInputStream, "D:/poi-test/openOffice/docx","docx");
        coc2HtmlUtil.file2pdf(fileInputStream, "D:/poi-test/openOffice/docx","docx");*/

    }

}

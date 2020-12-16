package com.digitzones.xml;

import com.digitzones.config.ERPConfig;
import com.digitzones.xml.model.Result;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

@Component
public class ParentUtil {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat ymdhms = new SimpleDateFormat("yyyyMMddHHmmss");
    private URL clientUrl ;
    private  String url;
    @Autowired
    private ERPConfig config;
    private HttpURLConnection conn;
    /**
     * 初始化
     * @return
     */
    public Result init(){
        Result result = new Result();
        result.setStatusCode(200);
        url = config.getUrl();
        try {
            clientUrl = new URL(url);
            conn = (HttpURLConnection)clientUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(100000);
            conn.setReadTimeout(100000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-type", "text/xml");
        } catch (IOException e) {
            e.printStackTrace();
            result = new Result();
            result.setMessage("操作失败，无法连接到ERP服务!");
            result.setStatusCode(300);
        }
        return result;
    }
    /**
     * 检查返回结果
     * @param xml
     * @param result
     */
    public void checkResult(String xml,Result result){
        OutputStream op = null;
        try {
            op = conn.getOutputStream();
            op.write(xml.getBytes("UTF-8"));
            op.flush();
            System.err.println(getConn().getResponseCode());
            if (getConn().getResponseCode() == 200) {
                InputStream in = getConn().getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
                String line = "";
                String message = "";
                while((line=bufferedReader.readLine())!=null){
                    message+=line;
                }
                Document doc = DocumentHelper.parseText(message);
                Element element = doc.getRootElement();
                Iterator<Element> it = element.elementIterator();
                while(it.hasNext()){
                    Element element1 = it.next();
                    String tagName = element1.getName();
                    if("item".equals(tagName)){
                        String key = element1.attribute("succeed").getValue();
                        String msg = element1.attribute("dsc").getValue();
                        if("0".equals(key)){
                            result.setMessage("操作成功!");
                            result.setStatusCode(200);
                        }else {
                            result.setMessage(msg);
                            result.setStatusCode(300);
                            System.err.println("ERP服务器返回码:"+key);
                        }
                        break;
                    }
                }
            }else{
                result.setStatusCode(300);
                result.setMessage("操作失败，与ERP服务器连接中断!");
                writeXml(xml);
                throw new RuntimeException();
            }
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            result.setMessage(e.getMessage());
            result.setStatusCode(300);
            writeXml(xml);
        }finally {
            try {
                if(op!=null) {
                    op.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //将xml写入到本地文件
    private void writeXml(String xml){
        File dir = new File("d:/dimes_log");
        if(!dir.exists()){
            dir.mkdir();
        }
        File file = new File(dir,ymdhms.format(new Date()));
        PrintWriter writer = null;
        try {
             writer = new PrintWriter(new FileOutputStream(file));
             writer.write(xml);
             writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(writer!=null){
                writer.close();
            }
        }
    }

    public SimpleDateFormat getFormat() {
        return format;
    }

    public void setFormat(SimpleDateFormat format) {
        this.format = format;
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    public URL getClientUrl() {
        return clientUrl;
    }

    public void setClientUrl(URL clientUrl) {
        this.clientUrl = clientUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ERPConfig getConfig() {
        return config;
    }

    public void setConfig(ERPConfig config) {
        this.config = config;
    }

    public HttpURLConnection getConn() {
        return conn;
    }

    public void setConn(HttpURLConnection conn) {
        this.conn = conn;
    }
}

package com.digitzones.util;
/**
 * 实现局域网内共享目录访问工具
 * @author Administrator
 */
import java.io.IOException;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
public class SMBUtil {
	/**
	 * 向共享目录指定文件中写数据
	 * @param remoteIp  远程服务器IP
	 * @param remoteUser 远程服务器用户名
	 * @param remotePass 远程服务器登录密码
	 * @param remoteFilename 共享文件名称，完整路径，如：/share/test.txt
	 * @param fileContent  文件内容
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public static void upload2SharedDir(String remoteIp,String remoteUser,String remotePass,String remoteFilename,byte[] fileContent)throws IOException {
		SmbFile smbFileOut = null;;
		SmbFileOutputStream out  = null;
		try {
			smbFileOut = new SmbFile("smb://"+remoteUser + ":" + remotePass + "@"+remoteIp + remoteFilename);
			if(!smbFileOut.exists())
				smbFileOut.createNewFile();
			out = new SmbFileOutputStream(smbFileOut);
			out.write(fileContent);
		}finally {
			if(smbFileOut!=null)
				smbFileOut.close();
			if(out!=null)
				out.close();
		}
	}
	public static void main(String[] args) throws IOException {
		//upload2SharedDir("192.168.10.199", "administrator", "a", "/tm2/TM2.txt", "bcd".getBytes());
	}
}

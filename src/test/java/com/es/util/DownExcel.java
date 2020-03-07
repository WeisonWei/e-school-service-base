package com.es.util;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownExcel {
	public static void downFile(String filepath,String fileName, HttpServletRequest request, HttpServletResponse response) {
		try {
			File temFile = new File(filepath);
			if (!temFile.exists()){
				response.getWriter().write("ERROR:文件没有找到");
				return;
			}
			String browser = request.getHeader("user-agent");
			Pattern p = Pattern.compile(".*MSIE.*?;.*");
			Matcher m = p.matcher(browser);
			// 设置response的Header   
			if (m.matches()) {
				response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8").replace("+", ""));
			} else {
				response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1").replace(" ", ""));
			}
			response.setHeader("Cache-Control", "max-age=" + 100);
			response.setContentLength((int) temFile.length());
			response.setContentType("application/x-download");
			response.setHeader("windows-Target", "_blank");
			// 以流的形式下载文件。
			OutputStream ot = response.getOutputStream();
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(temFile));
			BufferedOutputStream bos = new BufferedOutputStream(ot);
			byte[] buffer = new byte[4096];
			int length = 0;
			while ((length = bis.read(buffer)) > 0) {
				bos.write(buffer, 0, length);
			}
			bos.close();
			bis.close();
			ot.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	
	public static boolean	flag;
	public static File		file;

	/** 
	 *  根据路径删除指定的目录或文件，无论存在与否 
	 *@param sPath  要删除的目录或文件 
	 *@return 删除成功返回 true，否则返回 false。 
	 */
	public static boolean deleteFolder(String sPath) {
		flag = false;
		file = new File(sPath);
		// 判断目录或文件是否存在  
		if (!file.exists()) { // 不存在返回 false  
			return flag;
		} else {
			// 判断是否为文件  
			if (file.isFile()) { // 为文件时调用删除文件方法  
				return deleteFile(sPath);
			} else { // 为目录时调用删除目录方法  
				return deleteDirectory(sPath);
			}
		}
	}

	/** 
	 * 删除单个文件 
	 * @param   sPath    被删除文件的文件名 
	 * @return 单个文件删除成功返回true，否则返回false 
	 */
	public static boolean deleteFile(String sPath) {
		flag = false;
		file = new File(sPath);
		// 路径为文件且不为空则进行删除  
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/** 
	 * 删除目录（文件夹）以及目录下的文件 
	 * @param   sPath 被删除目录的文件路径 
	 * @return  目录删除成功返回true，否则返回false 
	 */
	public static boolean deleteDirectory(String sPath) {
		//如果sPath不以文件分隔符结尾，自动添加文件分隔符  
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		//如果dir对应的文件不存在，或者不是一个目录，则退出  
		if (!dirFile.exists() || !dirFile.isDirectory()) { return false; }
		flag = true;
		//删除文件夹下的所有文件(包括子目录)  
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			//删除子文件  
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag) break;
			} //删除子目录  
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag) break;
			}
		}
		if (!flag) return false;
		//删除当前目录  
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @describe   excel导出
	 * @author     zhangmk
	 * @param      workbook  excel对象
	 * @param      fileName 文件名
	 * @date       2019/1/17 20:08
	 */
	public static void downExcelFile(Workbook workbook, String fileName){
		OutputStream out = null;
		try {
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			HttpServletResponse response = requestAttributes.getResponse();
			String browser = request.getHeader("user-agent");
			Pattern p = Pattern.compile(".*MSIE.*?;.*");
			Matcher m = p.matcher(browser);
			// 设置response的Header
			if (m.matches()) {
				response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8").replace("+", ""));
			} else {
				response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1").replace(" ", ""));
			}
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			out = response.getOutputStream();
			workbook.write(out);
		}catch (IOException e){
			e.printStackTrace();
		}finally {
			if(out!=null){
				try {
					out.close();
				}catch (IOException e){
					e.printStackTrace();
				}
			}
		}
	}
}

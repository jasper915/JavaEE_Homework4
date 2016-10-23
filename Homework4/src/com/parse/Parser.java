package com.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

	public void Jsp2Servlet() {

		File servletFile = new File("demo_jsp.java");
		File JspFile = new File("demo.jsp");
		try {
			PrintStream ps = new PrintStream(servletFile);
			FileReader fr = new FileReader(JspFile);
			BufferedReader br = new BufferedReader(fr);

			ps.print("package Servlet;"
					+ "\n"
					+ "import java.io.IOException;"
					+ "\n"
					+ "import java.io.OutputStream;"
					+ "\n"
					+ "import javax.servlet.Servlet;"
					+ "\n"
					+ "import javax.servlet.ServletConfig;"
					+ "\n"
					+ "import javax.servlet.ServletException;"
					+ "\n"
					+ "import javax.servlet.ServletRequest;"
					+ "\n"
					+ "import javax.servlet.ServletResponse;"
					+ "\n"
					+ "import ServerContainer.MyServletResponse;"
					+ "\n"
					+ "public class Login implements Servlet {"
					+ "\n"// ´ýÐÞ¸Ä
					// + "@Override"
					+ "public void destroy() {"
					+ "\n"
					// + "// TODO Auto-generated method stub"
					+ "}"
					+ "\n"
					// + "@Override"
					+ "public ServletConfig getServletConfig() {"
					+ "\n"
					// + "// TODO Auto-generated method stub"
					+ "return null;"
					+ "\n"
					+ "}"
					+ "\n"
					// + "@Override"
					+ "public String getServletInfo() {"
					+ "\n"
					// + "// TODO Auto-generated method stub"
					+ "return null;"
					+ "\n"
					+ "}"
					+ "\n"
					// + "@Override"
					+ "public void init(ServletConfig arg0) throws ServletException {"
					+ "\n"
					// + "// TODO Auto-generated method stub"
					+ "}"
					+ "\n"
					// + "@Override"
					+ "public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {"
					+ "\n"
					+ "MyServletResponse res = (MyServletResponse) response;"
					+ "\n" + "OutputStream out = res.getOutputStream1();"
					+ "\n");
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null)
				sb.append(line);

			String str = sb.toString();
			String HTML_REGEX = "<html>.*?</html>";
			Pattern html_pattern = Pattern.compile(HTML_REGEX);
			Matcher html_matcher = html_pattern.matcher(str);

			if (html_matcher.find()) {
				String htmlStr = html_matcher.group();
				int startHtml = 0;
				int endHtml = htmlStr.length();

				String JSP_REGEX = "<%.*?%>";
				Pattern jsp_pattern = Pattern.compile(JSP_REGEX);
				Matcher jsp_matcher = jsp_pattern.matcher(htmlStr);

				int startJsp = startHtml;
				int endJsp = endHtml;
				while (jsp_matcher.find()) {
					String jspStr = jsp_matcher.group();
					startJsp = jsp_matcher.start();
					String pre = htmlStr.substring(startHtml, startJsp);
					ps.print("out.write(\"" + pre + "\".getBytes());" + "\n");
					endJsp = jsp_matcher.end();
					String regex = "<%[ ]*=";
					Matcher matcher = Pattern.compile(regex).matcher(jspStr);
					String jsp = null;
					if (matcher.find()) {
						jsp = jspStr.substring(jspStr.indexOf("=") + 1,
								jspStr.length() - 2);
						ps.print("out.write(" + jsp + ".getBytes());" + "\n");
					} else {
						jsp = htmlStr.substring(startJsp + 2, endJsp - 2);
						ps.print(jsp + "\n");
					}
					startHtml = endJsp;
				}
				String left = htmlStr.substring(startHtml, endHtml);
				ps.print("out.write(\"" + left + "\".getBytes());" + "\n");
			}

			ps.print("   }" + "\n" + "}" + "\n");

			ps.flush();
			ps.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

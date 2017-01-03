/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.xtec.web.clic.userlib;

import edu.xtec.web.clic.Utilities;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Francesc Busquets <francesc@gmail.com>
 */
public class DownloadProject extends HttpServlet {

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String err = null;
    String user, prj;
    String project = Utilities.getParam(request, "prj", null);
    int p = (project == null ? -1 : project.indexOf("/")) ;

    if (p > 1 &&
        UserProject.isValidName((user = project.substring(0, p))) &&
        UserProject.isValidName((prj = project.substring(p + 1)))) {

      File projectBase = new File(new File(GetUserInfo.getRootBase(), user), prj);
      int basePathLength = (projectBase.getCanonicalPath() + File.separator).length();
      Collection files = FileUtils.listFiles(projectBase, null, true);
      if (files.isEmpty()) {
        err = "Invalid project";
      } else {
        response.setHeader("Content-disposition", "attachment; filename=" + prj + ".scorm.zip");
        response.setContentType("application/octet-stream");
        ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
        Iterator it = files.iterator();
        while (it.hasNext()) {
          File f = (File) it.next();
          String fName = f.getCanonicalPath().substring(basePathLength);
          ZipEntry entry = new ZipEntry(fName);
          out.putNextEntry(entry);
          out.write(FileUtils.readFileToByteArray(f));
          out.closeEntry();
        }
        out.close();
        response.getOutputStream().flush();
      }
    } else {
      err = "Invalid request";
    }

    if (err != null) {
      throw new IOException(err);
    }

  }

}

<%--
  Created by IntelliJ IDEA.
  User: opw
  Date: 27/3/2016
  Time: 12:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>--%>
<%@ page import="SE.*" %>
<%@ page import="jdbm.RecordManager" %>
<%@ page import="jdbm.RecordManagerFactory" %>
<%@ page import="java.util.*" %>

<html>
<head>
    <title>Title</title>
</head>
<body>
<div>
    <a href="../">Back to search page</a>
</div>
<%
    if(request.getParameter("q")!=null)
    {
        String query = request.getParameter("q");
        out.println("Original Query: "+ query);
        String[] queryArr = query.split("\\s+");    // split by spacebar
        Vector<String> queryVector = new Vector<String>();
        // add the query to vector
        for (int i = 0; i < queryArr.length; i++) {
            queryVector.add(queryArr[i]);
        }

        for (int i = 0; i < queryArr.length; i++) {
            out.println("<br>");
            out.println(queryArr[i]);
        }
        out.println("<hr>");


        SearchEngine se = new SearchEngine();
        Map<Integer, Double> resultMap = se.search(queryVector);
        if (resultMap == null || resultMap.isEmpty()) {
            out.println("<p> Sorry, no matched result :( (Please try another query) </p>");
            return;
        }

        final int MAX_NUM_PAGES_RETURN = 50;
        int i = 1;

        RecordManager recman = RecordManagerFactory.createRecordManager(SearchEngine.DB_PATH);
        PageProperty ppt = new PageProperty(recman, "pagePropertyIndex");
        ForwardIndex forwardIndex = new ForwardIndex(recman, "forwardIndex");
        // loop result map
        for (Map.Entry<Integer, Double> entry : resultMap.entrySet()) {
            if( i > 50 )    break;  // get the top 50 results
            int pageID = entry.getKey();
            double score = entry.getValue();
            out.println(i + ") ");
            out.println("<strong>" + ppt.getTitle(pageID) + "</strong>");
            out.println("<br>");

            out.println("<a href=\"" + ppt.getUrl(pageID) + "\">" + ppt.getUrl(pageID) + "</a>");
            out.println("<pre>pageID:" + pageID + " score:" + score);
            out.println("Date: " + ppt.getModDate(pageID));
            out.println("Size: " + ppt.getPageSize(pageID));
            out.println(forwardIndex.getPageTermFrequencyString(pageID));
            out.println("</pre> <br>");
            ++i;
        }
    }
    else
    {
        out.println("ERROR: NO INPUT");
    }

%>
</body>
</html>

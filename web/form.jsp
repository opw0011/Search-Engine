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
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.Map" %>

<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    if(request.getParameter("q")!=null)
    {
        String query = request.getParameter("q");
        out.println("Original Query: "+ query);
        String[] queryArr = query.split("\\s+");
        Vector<String> queryVector = new Vector<String>();
        for (int i = 0; i < queryArr.length; i++) {
            queryVector.add(queryArr[i]);
        }

        out.println("<hr>");
        for (int i = 0; i < queryArr.length; i++) {
            out.println("<br>");
            out.println(queryArr[i]);
        }

        SearchEngine se = new SearchEngine();
        Map<Integer, Double> resultMap = se.search(queryVector);
        if (resultMap == null || resultMap.isEmpty()) {
            out.println("Sorry, no matched result :( (Please try another query)");
            return;
        }

        final int MAX_NUM_PAGES_RETURN = 50;
        int i = 1;
        // loop result map
        for (Map.Entry<Integer, Double> entry : resultMap.entrySet()) {
            if( i > 50 )    break;  // get the top 50 results
            int pageID = entry.getKey();
            double score = entry.getValue();
            out.println(i + " pageID:" + pageID + " score:" + score);
            out.println("<br>");
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

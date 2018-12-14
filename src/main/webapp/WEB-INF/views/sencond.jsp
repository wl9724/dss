<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
</head>

<body>
    <div style="text-align:center;padding: 50px">
        <div style="display:inline"><a href="first">第一题</a></div>
        <div style="display:inline"><a href="sencond">第二题</a></div>
    </div>
    
    <div>
        <div style="width:50%;float: left;">
            <table align="center" border="1px" cellspacing="0">
                <tr>
                    <th>客户号</th>
                    <th>2008年（万元）</th>
                    <th>2009年（万元）</th>
                    <th>2010年（万元）</th>
                    <th>将来业务量（万元）</th>
                </tr>
                <c:forEach items="${custrunover}" var="lift">
                    <tr>
                        <td>${lift.cusNum}</td>
                        <td>${lift.trunover08}</td>
                        <td>${lift.trunover09}</td>
                        <td>${lift.trunover10}</td>
                        <td>${lift.futureVolume}</td>

                    </tr>
                </c:forEach>
            </table>
        </div>
        <div id="container" style="height: 500%;width:50%;float: left;"></div>
    </div>
    <div>
        <table align="center" border="1px" cellspacing="0">
            <tr>
                <th>客户号</th>
                <c:forEach items="${custrunover}" var="lift">
                    <th>${lift.cusNum}</th>
                </c:forEach>
            </tr>
            <tr>
                <td>类别</td>
                <c:forEach items="${kmeans}" var="lift">
                    <td>${lift}</td>
                </c:forEach>
                
            </tr>
        </table>
    </div>
    
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/echarts.min.js"></script>
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts-gl/echarts-gl.min.js"></script>
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts-stat/ecStat.min.js"></script>
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/extension/dataTool.min.js"></script>
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/map/js/china.js"></script>
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/map/js/world.js"></script>
    <script type="text/javascript" src="https://api.map.baidu.com/api?v=2.0&ak=ZUONbpqGBsYGXNIYHicvbAbM"></script>
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/extension/bmap.min.js"></script>
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/simplex.js"></script>
    <script type="text/javascript">
        var dom = document.getElementById("container");
        var myChart = echarts.init(dom);
        var app = {};
        option = null;
        option = {
            title: {
                text: '聚类分析结果',
                x: 'center'
            },
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: ['0','1','2','3']
            },
            series: [
                {
                    name: '类别',
                    type: 'pie',
                    radius: '55%',
                    center: ['50%', '60%'],
                    data: ${data},
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };
        ;
        if (option && typeof option === "object") {
            myChart.setOption(option, true);
        }
    </script>
</body>

</html>
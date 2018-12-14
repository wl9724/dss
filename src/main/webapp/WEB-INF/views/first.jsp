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
    <div style="text-align:center;padding:50px">
        <div style="display:inline"><a href="first">第一题</a></div>
        <div style="display:inline"><a href="sencond">第二题</a></div>
    </div>
    <h5>1.市场需求预测</h5>
    <div>
        <div style="width:50%;float: left;">
            <table align="center" border="1px" cellspacing="0">
                <tr>
                    <th>年份</th>
                    <th>销售量（万台）</th>
                    <th>价格（元）</th>
                    <th>广告支出（万元）</th>
                    <th>汽车产量（万台）</th>
                </tr>
                <c:forEach items="${liftingjack}" var="lift">
                    <tr>
                        <td>${lift.year}</td>
                        <td>${lift.saleVolume}</td>
                        <td>${lift.price}</td>
                        <td>${lift.adExpenditure}</td>
                        <td>${lift.carOutput}</td>

                    </tr>
                </c:forEach>
            </table>
        </div>
        <div id="container" style="height: 300px; margin: 0;width: 50%;float: left;"></div>
    </div>
    <div>
        <label>销售预测模型：</label><span>${lines}</span>
        <p>当广告支出为126.2486万元时或价格下降到38.42元时,可以达到销售童增长大约10%的目标</p>
    </div>
    <h5>2.广告媒体选择决策</h5>
    <p>X1 ----- 分配给户外广告的设置次数 X2 ----- 分配给专业杂志的刊登次数 X3 ----- 分配给其他广告形式的次数</p>
    <label>决策模型:</label><span>Max E(X)=192*X1+36*X2+12*X3</span>
    <p>约束条件：</p>
    <p>150000*X1+24000*X2+120000*X3≤1000000</p>
    <p>150000*X1≥650000</p>
    <p>X1≥2</p>
    <p>X2≥3</p>
    <p>X3≤2</p>
    <label>规划求解结果</label>
    <span>X1=4.33,X2=14.83,X3=0,E=1357,取整得: X1=4,X2=15,X3=0,E=1357</span>
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
        var data = ${data};

        // See https://github.com/ecomfe/echarts-stat
        var myRegression = ecStat.regression('linear', data);

        myRegression.points.sort(function (a, b) {
            return a[0] - b[0];
        });

        option = {
            title: {
                text: '销售量和广告支出的关系曲线',
                subtext: 'By ecStat.regression',
                sublink: 'https://github.com/ecomfe/echarts-stat',
                left: 'center'
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross'
                }
            },
            xAxis: {
                type: 'value',
                splitLine: {
                    lineStyle: {
                        type: 'dashed'
                    }
                },
            },
            yAxis: {
                type: 'value',
                min: 1.5,
                splitLine: {
                    lineStyle: {
                        type: 'dashed'
                    }
                },
            },
            series: [{
                name: 'scatter',
                type: 'scatter',
                label: {
                    emphasis: {
                        show: true,
                        position: 'left',
                        textStyle: {
                            color: 'blue',
                            fontSize: 16
                        }
                    }
                },
                data: data
            }, {
                name: 'line',
                type: 'line',
                showSymbol: false,
                data: myRegression.points,
                markPoint: {
                    itemStyle: {
                        normal: {
                            color: 'transparent'
                        }
                    },
                    label: {
                        normal: {
                            show: true,
                            position: 'left',
                            formatter: myRegression.expression,
                            textStyle: {
                                color: '#333',
                                fontSize: 14
                            }
                        }
                    },
                    data: [{
                        coord: myRegression.points[myRegression.points.length - 1]
                    }]
                }
            }]
        };;
        if (option && typeof option === "object") {
            myChart.setOption(option, true);
        }
    </script>
</body>

</html>
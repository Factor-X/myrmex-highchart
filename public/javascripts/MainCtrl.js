var myApp = angular.module('app', []);

myApp.controller('MainCtrl', function ($scope, $http, $sce) {

        $scope.loading = true;

        // chart params
        $scope.chartWidth = 900;
        $scope.chartheight = 300;

        $scope.params = {
            chart: {
                type: 'spline',
                inverted: true,
                width:$scope.chartWidth,
                height:$scope.chartheight
            },
            title: {
                text: 'Atmosphere Temperature by Altitude'
            },
            subtitle: {
                text: 'According to the Standard Atmosphere Model'
            },
            xAxis: {
                reversed: false,
                title: {
                    enabled: true,
                    text: 'Altitude'
                },
                labels: {
                    formatter: function () {
                        return this.value + 'km';
                    }
                },
                maxPadding: 0.05,
                showLastLabel: true
            },
            yAxis: {
                title: {
                    text: 'Temperature'
                },
                labels: {
                    formatter: function () {
                        return this.value + '°';
                    }
                },
                lineWidth: 2
            },
            legend: {
                enabled: false
            },
            tooltip: {
                headerFormat: '<b>{series.name}</b><br/>',
                pointFormat: '{point.x} km: {point.y}°C'
            },
            plotOptions: {
                spline: {
                    marker: {
                        enable: false
                    }
                }
            },
            series: [{
                name: 'Temperature',
                data: [[0, 15], [10, -50], [20, -56.5], [30, -46.5], [40, -22.1],
                    [50, -2.5], [60, -27.7], [70, -55.7], [80, -76.5]]
            }]
        };

        // generate the chart
        $('#container').highcharts(angular.copy($scope.params));

        //load the chart svg
        $http({
            'method': 'POST',
            'url': '/chart/generate',
            'headers': "Content-Type:application/json",
            'data':{
                width:$scope.chartWidth,
                height:$scope.chartHeight,
                type:'svg',
                options:JSON.stringify($scope.params)
            }
        }).success(function (data, status) {
            $scope.img = $sce.trustAsHtml(data);
            $scope.loading = false;
        })
        .error(function (data, status) {
            $scope.error = data;
            $scope.loading = false;
        });
    }
);
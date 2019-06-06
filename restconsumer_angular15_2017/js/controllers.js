angular.module('mnsux.controllers', ['angularUtils.directives.dirPagination'])



.controller('forgotpwController', function($window, $scope, $cookies, $rootScope, $location, mnsAPI) {

	$rootScope.isLogin = mnsAPI.verifyLogin();

	dataLayer.push({
		'event':'VirtualPageView',
		'virtualPageURL': '/forgotpw',
		'virtualPageTitle' : 'Forgot Password'
	});

    // function to submit the form after all validation has occurred
    $scope.submitForgotpwForm = function() {

    	$scope.$broadcast('show-errors-check-validity');

        if ($scope.forgotpwForm.$valid) {

            // start spinner
            $scope.loading = true;

            mnsAPI.emailPassword($scope.email)

            .success(function (response) {

				if(response == "SUCCESS") {
				   $scope.forgotpwMessage  = "Password is sent at your registerred email address";
				}
				else if(response == "ERROR_FINDPASSWORD") {
					$scope.forgotpwMessage  = "Password could not be found for this email address";
				}
            	else  {
					$scope.errorMessage = "Sorry, password could not be sent due to system error";
				}

                // stop spinner
                $scope.loading = false;

				$('#myModal').modal('show');

            })

            .error(function (response) {
                //$('.errorMsg').show();
                $scope.errorMessage  = "Sorry, password could not be retrieved due to system error";
                // stop spinner
                $scope.loading = false;
                $window.scrollTo(0, 0);
            });


        } else {
            return;
        }
    };

       $('#myModal').on('hidden.bs.modal', function () {
		$location.path('/login/');
		$scope.$apply();
	});

})


.controller('reviewController', function($window, $scope,$cookieStore ,$rootScope ,$location, mnsAPI) {

	$rootScope.isLogin = mnsAPI.verifyLogin();
	$scope.reviewRequest ={};
    var url = window.location.href;
    var userId = url.substring(url.lastIndexOf('/')+1);

    /*
    $rootScope.captchaResponse = null;
    $rootScope.captchaRequiredError = false;
    */

	dataLayer.push({
		'event':'VirtualPageView',
		'virtualPageURL': '/addreview',
		'virtualPageTitle' : 'Add Maker Review'
	});

	/*
    $scope.setResponse = function (response) {
    	$rootScope.captchaRequiredError = false;
        $rootScope.captchaResponse = response;
        console.log("captcha response>>>>>>>" + response);
    };
	*/

    //function to submit the form after all validation has occurred
    $scope.submitReviewForm = function() {

    	$scope.$broadcast('show-errors-check-validity');

    	/*
	  	if($rootScope.captchaResponse == null) {
	      	   $rootScope.captchaRequiredError = true;
	      	   return;
      	}
		*/

        if ($scope.reviewForm.$valid) {

            $scope.loading = true;

        	dataLayer.push({
        		'userId' : userId,
        		'event':'addMakerReview'
        		});

            mnsAPI.postReview($scope.reviewRequest, userId)
        	.success(function (response) {
        		$('#myModal').modal('show');
        		$window.localStorage.setItem('reviewRequest', angular.toJson(response.reviewRequest));
        		$scope.loading = false;
        	})
        	.error(function (response) {
                $scope.loading = false;
                $('#myModal1').modal('show');
            });


        } else {
        	return;
        }

    };


    $('#myModal').on('hidden.bs.modal', function () {
		$location.path('/viewprofile/' + userId)
		$scope.$apply();

	});

})



.controller('logoutController', function($scope, $location, $cookies, $rootScope, mnsAPI) {
	mnsAPI.logoutUser();
	mnsAPI.setUser(null);
	$rootScope.isLogin = mnsAPI.verifyLogin();
	$rootScope.loggedinIdUser = 0;
	$rootScope.token = "";
	$location.path('/');
})

.controller('searchArticlesController', function($window, $scope, $cookies,$rootScope, $location, mnsAPI) {


	$rootScope.isLogin = mnsAPI.verifyLogin();
	$scope.serviceTypes = mnsAPI.getServiceTypes();
	$scope.loggedinIdUser = $rootScope.loggedinIdUser;

	dataLayer.push({
		'event':'VirtualPageView',
		'virtualPageURL': '/searcharticles',
		'virtualPageTitle' : 'Article Search Results'
	});

	if($location.search().idMaker != undefined){
		$scope.idMaker= $location.search().idMaker;
		$scope.currentPage = 1;
		$scope.pageSize = 10;
		$scope.zeroarticleSearchResultsMessage = "";

		dataLayer.push({
			'makerId' : $scope.idMaker,
			'event':'articleSearchByMakerId'
		});

		$scope.loading = true;
		mnsAPI.getArticleSearchResultsIdMaker($scope.idMaker)
			.success( function(response) {
				$scope.articleSearchResults = response;

				if(!$scope.articleSearchResults.length){
					$scope.zeroarticleSearchResultsMessage = "No Article found for your search. Please try again!"
					dataLayer.push({
						'makerId' : $scope.idMaker,
						'event':'articleNotFoundByMakerId'
					});
				}
				$scope.loading = false;
			})
			.error(function(error) {
				$scope.loading = false;
				$scope.zeroarticleSearchResultsMessage = "Sorry, could not find articles due to system error.";
			});
	}else{
		$scope.articleSearchCriteria ={};
		if($location.search().serviceIds != undefined){
			$scope.articleSearchCriteria.serviceIds = $location.search().serviceIds;
		}

		if($location.search().keywords != undefined){
			$scope.articleSearchCriteria.keywords = decodeURI($location.search().keywords);
		}

		dataLayer.push({
			'serviceIds' : $scope.articleSearchCriteria.serviceIds,
			'articleKeywords' : $scope.articleSearchCriteria.keywords,
			'event':'articleSearch'
		});


		$scope.currentPage = 1;
		$scope.pageSize = 10;
		$scope.zeroarticleSearchResultsMessage = "";

		$scope.loading = true;

		mnsAPI.getArticleSearchResults($scope.articleSearchCriteria)
			.success( function(response) {
				$scope.articleSearchResults = response;

				if(!$scope.articleSearchResults.length){
					$scope.zeroarticleSearchResultsMessage = "No Article found for your search. Please try again!"
					dataLayer.push({
						'serviceIds' : $scope.articleSearchCriteria.serviceIds,
						'articleKeywords' : $scope.articleSearchCriteria.keywords,
						'event':'articleNotFoundBySearch'
					});
				}
				$scope.loading = false;
			})
			.error(function(error) {
				$scope.loading = false;
				$scope.zeroarticleSearchResultsMessage = "Sorry, could not find articles due to system error.";
			});
	}

	$scope.articleSearchForm = function(){
		if(($scope.articleSearchCriteria.keywords =="" || $scope.articleSearchCriteria.keywords ==undefined) && ($scope.articleSearchCriteria.serviceIds =="" || $scope.articleSearchCriteria.serviceIds ==undefined)){
			$location.path('/searcharticles').search()
		}else{
			$location.path('/searcharticles').search({
				   				'serviceIds' : $scope.articleSearchCriteria.serviceIds,
				   				'keywords' : $scope.articleSearchCriteria.keywords
			   					});
		}

	}

	$scope.viewArticle = function(id){
		$window.open('/#/viewarticle/' +  id, '_self');
	}
})

.controller('viewArticleController', function($window, $scope, $cookies,$rootScope, $location, $sce, mnsAPI) {


	$rootScope.isLogin = mnsAPI.verifyLogin();
	$scope.serviceTypes = mnsAPI.getServiceTypes();
	$scope.loggedinIdUser = $rootScope.loggedinIdUser;

	dataLayer.push({
		'event':'VirtualPageView',
		'virtualPageURL': '/viewarticle',
		'virtualPageTitle' : 'Article View'
	});

	var url = window.location.href;
	var articleId = url.substring(url.lastIndexOf('/')+1);

	mnsAPI.getArticleDetail(articleId)
		.success(function(response) {
			$scope.article= response;
			mnsAPI.updateArticleCount(articleId,$scope.article.viewCount+1)
				.success(function(response) {
					$scope.loading = false;
				})
				.error(function(error) {
					$scope.loading = false;
			});
			$scope.article.serviceName =[];
			for (var i =0 ;i<$scope.article.serviceIds.length;i++){
				for(var j = 0;j<$scope.serviceTypes.length;j++){
					if($scope.article.serviceIds[i] == $scope.serviceTypes[j].idService){
						$scope.article.serviceName.push($scope.serviceTypes[j].serviceName);
					}
				}

			}
			if($scope.article.link !=null){
				if($scope.article.link.indexOf('youtube') != -1){
					$scope.youtubelink = true;
					var formattedYoutubeLink = $scope.article.link;
					formattedYoutubeLink = formattedYoutubeLink.substring(0, formattedYoutubeLink.lastIndexOf('&'));
					$scope.youLink =  $sce.trustAsResourceUrl(formattedYoutubeLink.replace("watch?v=", "embed/"));
				}
			}else if($scope.article.fileName != null){
				if($scope.article.format == 'pdf'){
					$scope.fileType = 'pdf';
					//to do: fix embedding in future
					//$scope.pdfLink =  $sce.trustAsResourceUrl("http://docs.google.com/gview?url=" + "http://" + mnsAPI.getRedirectDomain() + "/articles/" + articleId +"/"+ $scope.article.fileName + ".pdf&embedded=true");
					$scope.pdfLink =  "http://" + mnsAPI.getRedirectDomain() + "/articles/" + articleId +"/"+ $scope.article.fileName + ".pdf";
				}else if ($scope.article.format == 'html'){
					$scope.fileType = 'html';
				}
			}
			$scope.loading = false;
		})
		.error(function(error) {
			$scope.loading = false;
			alert('Sorry, could not find articles due to system error');
	});

})

;

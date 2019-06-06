angular.module('mnsux.services', [])

	.service('mnsAPI', function($http, $cookies, $rootScope){

		var baseDomain = "www.mydomain.com";
		var baseURL = "http://" + baseDomain + ":8080/mnsapi";

		var maker = {};
		var saver = {};
		var user = {};
		var loginRequest = {};
		var loginStatus = false;

		this.getLoginStatus = function() {
			return loginStatus;
		};

		this.setLoginStatus = function (givenLoginStatus) {
			loginStatus = givenLoginStatus;
			return;
		};

		this.verifyLogin = function() {
			if($cookies.get('idUser')){
				this.setLoginStatus(true);
			}
			return loginStatus;
		};


		this.validateToken = function(token, idUserInURL) {
			return $http({
				url: baseURL + '/security/validate',
				method: 'POST',
			  	headers: {
					"Content-Type": "application/vnd.captech-v1.0+json",
					'Accept': 'application/vnd.captech-v1.0+json'
			  	},
				data: {
					"token" : token,
					"userId" : idUserInURL
				}
		  	});
		};


		this.loginUser = function(token){
			$cookies.put('idUser', token, -1);
			$rootScope.isLogin = true;
			this.setLoginStatus(true);
			return;
		};




		this.logoutUser = function(){
			$cookies.remove('idUser');
			$rootScope.isLogin = false;
			this.setLoginStatus(false);
			return;
		};

		//Do not implement setBaseDomain method for security reasons

		this.getBaseDomain = function() {
			return baseDomain;
		};

		this.getBaseURL = function() {
			return baseURL;
		};



		this.getUser = function() {
			return user;
		};

		this.setUser = function(givenUser){
			if(givenUser != null) {
				user.idUser = givenUser.idUser;
				user.email = givenUser.email;
				user.password = givenUser.password;
				user.name = givenUser.name;
				user.contactAddress1 = givenUser.contactAddress1;
				user.contactAddress2 = givenUser.contactAddress2;
				user.contactCity = givenUser.contactCity;
				user.contactState = givenUser.contactState;
				user.contactZip = givenUser.contactZip;
				user.contactCountry = givenUser.contactCountry;
				user.phone1 = givenUser.phone1;
				user.phone2 = givenUser.phone2;
				user.makerType = true;
				user.saverType = false;
				user.maker = givenUser.maker;
				user.saverType = givenUser.saver;
			}
			return;
		};

		this.getMaker = function(){
			return maker;
		};

		this.setMaker = function(givenMaker){
			if (givenMaker != null) {
				maker.idMaker = givenMaker.idMaker;
				maker.logoFile = givenMaker.logoFile;
				maker.description = givenMaker.description;
				maker.website = givenMaker.website; //verify
				maker.viewCount = givenMaker.viewCount;
				maker.contactRequestCount = givenMaker.contactRequestCount;
				maker.articleCount = givenMaker.articleCount;
				maker.reviewCount = givenMaker.reviewCount;
				maker.reviewRank = givenMaker.reviewRank;
				maker.serviceIds = givenMaker.serviceIds;
				maker.logoFileBytes = givenMaker.logoFileBytes;
				maker.serviceNames = givenMaker.serviceNames;
			}
			return;
		};

		this.getSaver = function(){
			return saver;
		};

		this.setSaver = function(givenSaver){
			return;
		};



		this.getLoginRequest = function() {
			return loginRequest;
		};


		this.setLoginRequest = function(loginRequestData) {
			loginRequest.email = loginRequestData.login;
			loginRequest.password = loginRequestData.password;
			return;
		};

		this.getAllReview = function(idMaker) {
			return $http({
				method: 'get',
				url: baseURL+'/review/all/' + idMaker,
				headers: {
					'Accept': 'application/vnd.captech-v1.0+json',
					'Content-Type': 'application/vnd.captech-v1.0+json'
				},
				data: ''
			});
		};

		this.postReview = function(data,idMaker) {
			return $http({
				method: 'post',
				url: baseURL+'/review/',
				headers: {
					'Accept': 'application/vnd.captech-v1.0+json',
					'Content-Type': 'application/vnd.captech-v1.0+json'
				},
				data:{
						"idMaker": idMaker,
						"rating": data.rating,
						"title": data.title,
						"description": data.description,
						"reviewerName": data.reviewerName,
						"reviewerEmail": data.reviewerEmail,
						"reviewerZip": data.reviewerZip
					}
			});
		};

		this.getArticleSearchResults = function(givenSearchCriteria) {
			return $http({
				url: baseURL + '/article/search/',
				method: 'get',
				params: {
					"serviceIds": givenSearchCriteria.serviceIds,
					"keywords": givenSearchCriteria.keywords
				},
				headers: {
					'Content-Type': 'application/vnd.captech-v1.0+json' ,
					'Accept': 'application/vnd.captech-v1.0+json'
				},
				data: ''
			});

		};

		this.getArticleDetail = function(articleId) {
			return $http({
				url: baseURL + '/article/'+articleId,
				method: 'get',
				headers: {
					'Content-Type': 'application/vnd.captech-v1.0+json' ,
					'Accept': 'application/vnd.captech-v1.0+json'
				},
				data: ''
			});

		};

		this.updateArticleCount = function(articleId,viewCount) {
			return $http({
				url: baseURL + '/article/'+articleId,
				method: 'put',
				headers: {
					'Content-Type': 'application/vnd.captech-v1.0+json' ,
					'Accept': 'application/vnd.captech-v1.0+json'
				},
				data: {
					"id": articleId,
					"viewCount": viewCount
					}
			});

		};


	});
angular.module('mnsux', [
  'mnsux.services',
  'mnsux.controllers',
  'ngRoute',
  'ngCookies',
  'vcRecaptcha'
]).

config(['$routeProvider', function($routeProvider) {

  $routeProvider.

	when("/", {
		templateUrl: "partials/home.html",
		controller: "homeController"
	}).

	when("/login/", {
		templateUrl: "partials/login.html",
		controller: "loginController"
	}).

	when("/forgotpw/", {
		templateUrl: "partials/forgotpw.html",
		controller: "forgotpwController"
	}).

	when("/logout", {
      	templateUrl: "partials/home.html",
      	controller: "logoutController"
  	}).

	when("/addreview/:makerId", {
      	templateUrl: "partials/addreview.html",
      	controller: "reviewController"
  	}).

	when("/searcharticles", {
      	templateUrl: "partials/searcharticles.html",
      	controller: "searchArticlesController"
  	}).

	when("/viewarticle/:articleId", {
      	templateUrl: "partials/viewarticle.html",
      	controller: "viewArticleController"
  	}).

	when("/addarticle/:makerId", {
      	templateUrl: "partials/addarticle.html",
      	controller: "addArticleController"
  	}).

  	otherwise({redirectTo: '/'});
}]);


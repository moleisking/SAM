"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var core_1 = require("@angular/core");
var auth_1 = require("../services/auth");
var user_1 = require("../services/user");
var router_1 = require("@angular/router");
var Login = (function () {
    function Login(auth, user, router) {
        this.auth = auth;
        this.user = user;
        this.router = router;
        this.localUser = {
            name: "",
            pass: ""
        };
        this.message = "login";
    }
    Login.prototype.login = function () {
        var _this = this;
        this.auth.login(this.localUser).then(function () {
            _this.router.navigate(["/dashboard"]);
        }, function (res) {
            _this.message = "invalid user";
        });
    };
    Login.prototype.register = function () {
        var _this = this;
        this.user.register(this.localUser).then(function () {
            _this.message = "user registered";
        }, function (res) {
            _this.message = res;
        });
    };
    Login = __decorate([
        core_1.Component({
            selector: "login-component",
            templateUrl: "/views/login.html",
            providers: [user_1.UserService, auth_1.AuthService]
        }), 
        __metadata('design:paramtypes', [auth_1.AuthService, user_1.UserService, router_1.Router])
    ], Login);
    return Login;
}());
exports.Login = Login;

//# sourceMappingURL=login.js.map

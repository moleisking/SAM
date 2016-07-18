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
var router_1 = require("@angular/router");
var auth_1 = require("./auth");
var AuthManager = (function () {
    function AuthManager(authService, router) {
        this.authService = authService;
        this.router = router;
    }
    AuthManager.prototype.canActivate = function (next, state) {
        if (next.url[0].path === "login") {
            if (window.localStorage.getItem("auth_key")) {
                console.log("You already logged in");
                return false;
            }
            else
                return true;
        }
        if (this.authService.isLoggedIn)
            return true;
        console.log("You must be logged in");
        this.router.navigate(["/login"]);
        return false;
    };
    AuthManager = __decorate([
        core_1.Injectable(), 
        __metadata('design:paramtypes', [auth_1.AuthService, router_1.Router])
    ], AuthManager);
    return AuthManager;
}());
exports.AuthManager = AuthManager;

//# sourceMappingURL=auth.manager.js.map

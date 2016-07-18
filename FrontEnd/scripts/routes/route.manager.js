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
var auth_1 = require("../services/auth");
var RoutesManager = (function () {
    function RoutesManager(authService, router) {
        this.authService = authService;
        this.router = router;
    }
    RoutesManager.prototype.canActivate = function (next, state) {
        if (next.url[0].path === "login")
            if (localStorage.getItem("auth_key")) {
                console.log("You already logged in");
                this.router.navigate(["/dashboard"]);
                return false;
            }
            else
                return true;
        if (this.authService.isLoggedIn())
            return true;
        console.log("You must be logged in");
        this.router.navigate(["/login"]);
        return false;
    };
    RoutesManager = __decorate([
        core_1.Injectable(), 
        __metadata('design:paramtypes', [auth_1.AuthService, router_1.Router])
    ], RoutesManager);
    return RoutesManager;
}());
exports.RoutesManager = RoutesManager;

//# sourceMappingURL=route.manager.js.map

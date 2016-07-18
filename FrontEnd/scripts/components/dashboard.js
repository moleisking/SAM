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
var user_1 = require("../services/user");
var Dashboard = (function () {
    function Dashboard(authService, user, router) {
        this.authService = authService;
        this.user = user;
        this.router = router;
        this.message = "SAM";
    }
    Dashboard.prototype.logout = function () {
        this.authService.logout();
        this.router.navigate(["/login"]);
    };
    Dashboard.prototype.getAllUsers = function () {
        var _this = this;
        this.user.all().subscribe(function (users) { return _this.users = users; }, function (error) { return _this.message = error; });
    };
    Dashboard.prototype.ngOnInit = function () {
        this.getAllUsers();
    };
    Dashboard = __decorate([
        core_1.Component({
            selector: "dashboard-component",
            templateUrl: "/views/dashboard.html",
            providers: [auth_1.AuthService, user_1.UserService]
        }), 
        __metadata('design:paramtypes', [auth_1.AuthService, user_1.UserService, router_1.Router])
    ], Dashboard);
    return Dashboard;
}());
exports.Dashboard = Dashboard;

//# sourceMappingURL=dashboard.js.map

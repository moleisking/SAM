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
var http_1 = require("@angular/http");
var settings_1 = require("../config/settings");
var AuthService = (function () {
    function AuthService(http) {
        this.http = http;
    }
    AuthService.prototype.login = function (usercreds) {
        var _this = this;
        var creds = "name=" + usercreds.name + "&pass=" + usercreds.pass;
        var headers = new http_1.Headers();
        headers.append("Content-Type", "application/X-www-form-urlencoded");
        return new Promise(function (resolve, reject) {
            _this.http.post(settings_1.Settings.backend_url + "/api/authenticate", creds, { headers: headers }).subscribe(function (data) {
                if (data.json().success) {
                    localStorage.setItem("auth_key", data.json().token.split(" ")[1]);
                    resolve(true);
                }
                else {
                    console.log(data.json().message);
                    reject(data.json().message);
                }
            });
        });
    };
    AuthService.prototype.logout = function () {
        localStorage.removeItem("auth_key");
    };
    AuthService.prototype.isLoggedIn = function () {
        if (localStorage.getItem("auth_key") !== null)
            return true;
    };
    AuthService = __decorate([
        core_1.Injectable(), 
        __metadata('design:paramtypes', [http_1.Http])
    ], AuthService);
    return AuthService;
}());
exports.AuthService = AuthService;

//# sourceMappingURL=auth.js.map

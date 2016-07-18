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
var Rx_1 = require("rxjs/Rx");
var UserService = (function () {
    function UserService(http) {
        this.http = http;
    }
    UserService.prototype.register = function (usercreds) {
        var _this = this;
        var creds = "name=" + usercreds.name + "&pass=" + usercreds.pass;
        var headers = new http_1.Headers();
        headers.append("Content-Type", "application/X-www-form-urlencoded");
        return new Promise(function (resolve, reject) {
            _this.http.post(settings_1.Settings.backend_url + "/api/signup", creds, { headers: headers }).subscribe(function (data) {
                if (data.json().success)
                    resolve(true);
                else {
                    console.log(data.json().message);
                    reject(data.json().message);
                }
            });
        });
    };
    UserService.prototype.all = function () {
        var headers = new http_1.Headers();
        headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));
        return this.http.get(settings_1.Settings.backend_url + "/users", { headers: headers })
            .map(this.extractData)
            .catch(this.handleError);
    };
    UserService.prototype.extractData = function (res) {
        console.log(res);
        var body = res.json();
        return body.data || {};
    };
    UserService.prototype.handleError = function (error) {
        // In a real world app, we might use a remote logging infrastructure
        // We'd also dig deeper into the error to get a better message
        var errMsg = (error.message) ? error.message :
            error.status ? error.status + " - " + error.statusText : "Server error";
        console.error(errMsg); // log to console instead
        return Rx_1.Observable.throw(errMsg);
    };
    UserService = __decorate([
        core_1.Injectable(), 
        __metadata('design:paramtypes', [http_1.Http])
    ], UserService);
    return UserService;
}());
exports.UserService = UserService;

//# sourceMappingURL=user.js.map

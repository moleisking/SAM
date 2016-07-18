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
var WebService = (function () {
    function WebService(http) {
        this.http = http;
    }
    WebService.prototype.about = function () {
        var _this = this;
        return new Promise(function (resolve) {
            _this.http.get(settings_1.Settings.backend_url + "/about").subscribe(function (data) {
                if (data.json().success)
                    resolve(data.json().data);
                else {
                    console.log(data.json().message);
                    resolve(data.json().message);
                }
            });
        });
    };
    WebService = __decorate([
        core_1.Injectable(), 
        __metadata('design:paramtypes', [http_1.Http])
    ], WebService);
    return WebService;
}());
exports.WebService = WebService;

//# sourceMappingURL=web.js.map

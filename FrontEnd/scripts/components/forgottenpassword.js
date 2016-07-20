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
var forms_1 = require("@angular/forms");
var common_1 = require("@angular/common");
var user_1 = require("../services/user");
var ForgottenPassword = (function () {
    function ForgottenPassword(builder, formBuilder, user) {
        this.builder = builder;
        this.formBuilder = formBuilder;
        this.user = user;
        this.message = "Forgotten Password Text";
    }
    ForgottenPassword.prototype.ngOnInit = function () {
        this.myForm = this.formBuilder.group({
            email: ["", common_1.Validators.required]
        });
    };
    ForgottenPassword.prototype.send = function () {
        var _this = this;
        this.user.forgottenpassword(this.myForm.value)
            .subscribe(function (data) {
            _this.message = data.message;
        }, function (error) {
            _this.message = "Error sending email.";
        }, function () { return console.log("done forgotten password"); });
    };
    ForgottenPassword = __decorate([
        core_1.Component({
            selector: "forgottenpassword-component",
            templateUrl: "/views/forgottenpassword.html",
            styleUrls: ["/styles/forgottenpassword.css"],
            directives: [forms_1.REACTIVE_FORM_DIRECTIVES],
            providers: [user_1.UserService]
        }), 
        __metadata('design:paramtypes', [forms_1.FormBuilder, forms_1.FormBuilder, user_1.UserService])
    ], ForgottenPassword);
    return ForgottenPassword;
}());
exports.ForgottenPassword = ForgottenPassword;

//# sourceMappingURL=forgottenpassword.js.map

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
var forms_1 = require("@angular/forms");
var common_1 = require("@angular/common");
var auth_1 = require("../services/auth");
var user_1 = require("../services/user");
// import { User } from "../models/user";
var LoginFormComponent = (function () {
    function LoginFormComponent(auth, user, router, builder, _fb) {
        this.auth = auth;
        this.user = user;
        this.router = router;
        this.builder = builder;
        this._fb = _fb;
        this.message = "login messages here.";
    }
    LoginFormComponent.prototype.ngOnInit = function () {
        this.myForm = this._fb.group({
            name: ["", common_1.Validators.required],
            pass: ["", [common_1.Validators.required, common_1.Validators.minLength(3), common_1.Validators.maxLength(20)]]
        });
        // this.subcribeToFormChanges();
    };
    // subcribeToFormChanges() {
    //     const myFormStatusChanges$ = this.myForm.statusChanges;
    //     const myFormValueChanges$ = this.myForm.valueChanges;
    //     myFormStatusChanges$.subscribe(x => this.events.push({ event: "STATUS_CHANGED", object: x }));
    //     myFormValueChanges$.subscribe(x => this.events.push({ event: "VALUE_CHANGED", object: x }));
    // }
    LoginFormComponent.prototype.login = function () {
        var _this = this;
        this.submitted = true;
        console.log(this.myForm.value);
        this.auth.login(this.myForm.value).then(function () {
            _this.router.navigate(["/dashboard"]);
        }, function (res) {
            _this.message = "invalid user";
        });
    };
    LoginFormComponent.prototype.register = function () {
        var _this = this;
        this.submitted = true;
        console.log(this.myForm.value);
        this.user.register(this.myForm.value).then(function () {
            _this.message = "user registered";
        }, function (res) {
            _this.message = res;
        });
    };
    LoginFormComponent = __decorate([
        core_1.Component({
            selector: "login-form",
            providers: [user_1.UserService, auth_1.AuthService],
            templateUrl: "/views/login-form.html",
            styleUrls: ["/styles/login-form.css"],
            directives: [forms_1.REACTIVE_FORM_DIRECTIVES]
        }), 
        __metadata('design:paramtypes', [auth_1.AuthService, user_1.UserService, router_1.Router, forms_1.FormBuilder, forms_1.FormBuilder])
    ], LoginFormComponent);
    return LoginFormComponent;
}());
exports.LoginFormComponent = LoginFormComponent;

//# sourceMappingURL=login-form.js.map

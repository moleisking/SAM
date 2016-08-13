import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { REACTIVE_FORM_DIRECTIVES, FormBuilder } from "@angular/forms";
import { CUSTOM_ELEMENTS_SCHEMA } from "@angular/core";

import { AppComponent } from "./app.component";
import { AppRouterProvider } from "./routes/app.routes";
import { RoutesManager } from "./routes/route.manager";

import { AuthService } from "./services/auth";
import { CategoriesService } from "./services/categories";
import { UserService } from "./services/user";
import { WebService } from "./services/web";

import { Home } from "./components/home";
import { Login } from "./components/login";
import { Register } from "./components/register";
import { Dashboard } from "./components/dashboard";
import { AddAWork } from "./components/addawork";
import { Categories } from "./components/categories";
import { About } from "./components/about";
import { ContactUs } from "./components/contactus";
import { TermsConditions } from "./components/termsconditions";
import { ForgottenPassword } from "./components/forgottenpassword";
import { Profile } from "./components/profile";

import { AddAWorkFormComponent } from "./components/addawork-form";
import { ContactUsFormComponent } from "./components/contactus-form";
import { LoginFormComponent } from "./components/login-form";
import { ProfileFormComponent } from "./components/profile-form";
import { RegisterFormComponent } from "./components/register-form";

import { SELECT_DIRECTIVES } from "ng2-select";

import { HTTP_PROVIDERS } from "@angular/http";

import { environment } from "./environment";
import { enableProdMode } from "@angular/core";

if (environment.production)
    enableProdMode();

@NgModule({
    imports: [
        BrowserModule,
        // FormGroup,
        AppRouterProvider,
    ],
    bootstrap: [
        AppComponent,
    ],
    declarations: [
        AppComponent,

        REACTIVE_FORM_DIRECTIVES,
        SELECT_DIRECTIVES,

        Home,
        Login,
        Register,
        Dashboard,
        AddAWork,
        Categories,
        About,
        ContactUs,
        TermsConditions,
        ForgottenPassword,
        Profile,

        AddAWorkFormComponent,
        ContactUsFormComponent,
        LoginFormComponent,
        ProfileFormComponent,
        RegisterFormComponent,
    ],
    providers: [
        HTTP_PROVIDERS,
        FormBuilder,
        AuthService,
        CategoriesService,
        UserService,
        WebService,
        RoutesManager,
    ],
    schemas: [
        CUSTOM_ELEMENTS_SCHEMA
    ],
})

export class AppModule { }
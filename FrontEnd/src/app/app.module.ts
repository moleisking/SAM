import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { CUSTOM_ELEMENTS_SCHEMA } from "@angular/core";
import { RoutesManager } from "./routes/route.manager";
import { appRouterProvider } from "./routes/app.routes";

import { AppComponent } from "./app.component";
import { Home } from "./components/home";
import { Login } from "./components/login";
import { Register } from "./components/register";
import { Dashboard } from "./components/dashboard";
import { Categories } from "./components/categories";
import { About } from "./components/about";
import { ContactUs } from "./components/contactus";
import { TermsConditions } from "./components/termsconditions";
import { CookiePolicy } from "./components/cookiepolicy";
import { ForgottenPassword } from "./components/forgottenpassword";
import { Profile } from "./components/profile";
import { Messages } from "./components/messages";

import { ContactUsFormComponent } from "./components/contactus-form";
import { LoginFormComponent } from "./components/login-form";
import { ProfileFormComponent } from "./components/profile-form";
import { RegisterFormComponent } from "./components/register-form";

import { AuthService } from "./services/auth";
import { CategoriesService } from "./services/categories";
import { UserService } from "./services/user";
import { WebService } from "./services/web";
import { MessageService } from "./services/message";

import { SELECT_DIRECTIVES } from "ng2-select";

import { HTTP_PROVIDERS } from "@angular/http";

import { Settings } from "./config/settings";
import { enableProdMode } from "@angular/core";

if (Settings.prod) enableProdMode();

@NgModule({
    imports: [
        BrowserModule,
        ReactiveFormsModule,
        appRouterProvider,
    ],
    bootstrap: [
        AppComponent,
    ],
    declarations: [
        AppComponent,

        SELECT_DIRECTIVES,

        Home,
        Login,
        Register,
        Dashboard,
        Categories,
        About,
        ContactUs,
        TermsConditions,
        CookiePolicy,
        ForgottenPassword,
        Profile,
        Messages,

        ContactUsFormComponent,
        LoginFormComponent,
        ProfileFormComponent,
        RegisterFormComponent,
    ],
    providers: [
        HTTP_PROVIDERS,
        RoutesManager,
        AuthService,
        CategoriesService,
        UserService,
        WebService,
        MessageService,
    ],
    schemas: [
        CUSTOM_ELEMENTS_SCHEMA
    ],
})

export class AppModule { }
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
import { Profile } from "./components/profile";
import { Messages } from "./components/messages";

import { ForgottenPasswordFormComponent } from "./components/forgottenpassword-form";
import { ChangePasswordFormComponent } from "./components/changepassword-form";
import { ContactUsFormComponent } from "./components/contactus-form";
import { LoginFormComponent } from "./components/login-form";
import { ProfileFormComponent } from "./components/profile-form";
import { RegisterFormComponent } from "./components/register-form";

import { AuthService } from "./services/auth";
import { CategoriesService } from "./services/categories";
import { UserService } from "./services/user";
import { WebService } from "./services/web";
import { MessageService } from "./services/message";
import { RatingService } from "./services/rating";
import { PaymentService } from "./services/payment";

import { SelectComponent } from "ng2-select";
import { OffClickDirective } from "ng2-select/components/select/off-click";
import { HighlightPipe } from "ng2-select/components/select/select-pipes";
import { TabsModule } from "ng2-tabs";
import { RatingModule } from "ng2-rating";

import { HttpModule, Http } from "@angular/http";
import { TranslateModule, TranslateLoader, TranslateStaticLoader } from "ng2-translate";

import { Settings } from "./config/settings";
import { enableProdMode } from "@angular/core";

if (Settings.prod)
    enableProdMode();

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        HttpModule,
        ReactiveFormsModule,
        appRouterProvider,
        TabsModule,
        RatingModule,
        TranslateModule.forRoot({
            provide: TranslateLoader,
            useFactory: (http: Http) => new TranslateStaticLoader(http, "./localization", ".json"),
            deps: [Http]
        })
    ],
    bootstrap: [
        AppComponent,
    ],
    declarations: [
        AppComponent,

        SelectComponent,
        OffClickDirective,
        HighlightPipe,

        Home,
        Login,
        Register,
        Dashboard,
        Categories,
        About,
        ContactUs,
        TermsConditions,
        CookiePolicy,
        Profile,
        Messages,

        ForgottenPasswordFormComponent,
        ChangePasswordFormComponent,
        ContactUsFormComponent,
        LoginFormComponent,
        ProfileFormComponent,
        RegisterFormComponent,
    ],
    providers: [
        RoutesManager,
        AuthService,
        CategoriesService,
        UserService,
        WebService,
        MessageService,
        RatingService,
        PaymentService,
    ],
    schemas: [
        CUSTOM_ELEMENTS_SCHEMA
    ],
})

export class AppModule { }

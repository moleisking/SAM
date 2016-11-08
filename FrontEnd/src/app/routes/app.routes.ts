import { Routes, RouterModule } from "@angular/router";
import { RoutesManager } from "./route.manager";

import { Home } from "../components/home";
import { Login } from "../components/login";
import { Register } from "../components/register";
import { About } from "../components/about";
import { CookiePolicy } from "../components/cookiepolicy";
import { TermsConditions } from "../components/termsconditions";
import { ContactUs } from "../components/contactus";
import { Dashboard } from "../components/dashboard";
import { ForgottenPasswordFormComponent } from "../components/forgottenpassword-form";
import { Categories } from "../components/categories";
import { Profile } from "../components/profile";
import { Messages } from "../components/messages";

export const routes: Routes = [
  { path: "home", component: Home },
  { path: "login", component: Login, canActivate: [RoutesManager] },
  { path: "register", component: Register, canActivate: [RoutesManager] },
  { path: "dashboard", component: Dashboard, canActivate: [RoutesManager] },
  { path: "categories", component: Categories },
  { path: "about", component: About },
  { path: "contactus", component: ContactUs },
  { path: "termsconditions", component: TermsConditions },
  { path: "cookiepolicy", component: CookiePolicy },
  { path: "forgottenpassword", component: ForgottenPasswordFormComponent, canActivate: [RoutesManager] },
  { path: "messages/:id", component: Messages, canActivate: [RoutesManager] },
  { path: "profile/:id", component: Profile },
  { path: "", redirectTo: "/home", pathMatch: "full" }
];

export const appRouterProvider = RouterModule.forRoot(routes);

import { provideRouter, RouterConfig } from "@angular/router";
import { RoutesManager } from "./route.manager";

import { Home } from "../components/home";
import { Login } from "../components/login";
import { Register } from "../components/register";
import { About } from "../components/about";
import { TermsConditions } from "../components/termsconditions";
import { Dashboard } from "../components/dashboard";
import { ForgottenPassword } from "../components/forgottenpassword";

export const routes: RouterConfig = [
  { path: "home", component: Home },
  { path: "login", component: Login, canActivate: [RoutesManager] },
  { path: "register", component: Register, canActivate: [RoutesManager] },
  { path: "dashboard", component: Dashboard, canActivate: [RoutesManager] },
  { path: "about", component: About },
  { path: "contactus", component: About },
  { path: "termsconditions", component: TermsConditions },
  { path: "forgottenpassword", component: ForgottenPassword, canActivate: [RoutesManager] },
  { path: "", redirectTo: "home", pathMatch: "full" }
];

export const AppRouterProvider = provideRouter(routes);
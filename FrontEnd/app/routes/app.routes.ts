import { provideRouter, RouterConfig } from "@angular/router";
import { RoutesManager } from "./route.manager";

import { Login } from "../components/login";
import { About } from "../components/about";
import { Dashboard } from "../components/dashboard";
import { ForgottenPassword } from "../components/forgottenpassword";

export const routes: RouterConfig = [
  { path: "", redirectTo: "dashboard", pathMatch: "full" },
  { path: "login", component: Login, canActivate: [RoutesManager] },
  { path: "dashboard", component: Dashboard, canActivate: [RoutesManager] },
  { path: "about", component: About },
  { path: "forgottenpassword", component: ForgottenPassword, canActivate: [RoutesManager] }
];

export const AppRouterProvider = provideRouter(routes);
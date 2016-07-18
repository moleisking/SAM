import { bootstrap } from "@angular/platform-browser-dynamic";
import { AppComponent } from "./app.component";
import { enableProdMode } from "@angular/core";
import { HTTP_PROVIDERS } from "@angular/http";
import { AppRouterProvider } from "./routes/app.routes";
import { AuthService } from "./services/auth";
import { RoutesManager } from "./routes/route.manager";
import { environment } from "./environment";

if (environment.production) {
    enableProdMode();
}

bootstrap(AppComponent, [AppRouterProvider, RoutesManager, AuthService, HTTP_PROVIDERS])
    .then(success => console.log("Bootstrapped successfully!"))
    .catch(err => console.log(err));
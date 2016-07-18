import { Injectable } from "@angular/core";
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from "@angular/router";
import { AuthService } from "../services/auth";

@Injectable()

export class RoutesManager implements CanActivate {

    constructor(private authService: AuthService, private router: Router) { }

    canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        if (next.url[0].path === "login")
            if (localStorage.getItem("auth_key")) {
                console.log("You already logged in");
                this.router.navigate(["/dashboard"]);
                return false;
            }
            else
                return true;

        if (this.authService.isLoggedIn())
            return true;

        console.log("You must be logged in");
        this.router.navigate(["/login"]);
        return false;
    }
}
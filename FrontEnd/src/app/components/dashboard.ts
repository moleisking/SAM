import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { ROUTER_DIRECTIVES } from "@angular/router";

import { TAB_DIRECTIVES } from "ng2-tabs";
import { ProfileFormComponent } from "./profile-form";

import { AuthService } from "../services/auth";
import { UserService } from "../services/user";
// import { WorkService } from "../services/work";

import { UserModel } from "../models/user";
// import { WorkModel } from "../models/work";

@Component({
    selector: "dashboard-component",
    templateUrl: "../../views/dashboard.html",
    // providers: [AuthService, UserService, WorkService],
    providers: [AuthService, UserService],
    directives: [ROUTER_DIRECTIVES, TAB_DIRECTIVES, ProfileFormComponent]
})

export class Dashboard implements OnInit {

    private message: string;
    private messageWorks: string;
    private messageUsers: string;

    private users: UserModel[];
    // private works: WorkModel[];

    // constructor(private authService: AuthService, private user: UserService, private work: WorkService, private router: Router) {
    constructor(private authService: AuthService, private user: UserService, private router: Router) {
        this.message = "My Dashboard in SAM";
    }

    ngOnInit() {
        this.getAllUsers();
        // this.getMyWorks();
    }

    logout() {
        this.authService.logout();
        this.router.navigate(["/login"]);
    }

    getAllUsers() {
        this.user.all().subscribe(
            users => {
                this.users = users;
                console.log(users)
            },
            error => this.messageUsers = <any>error,
            () => console.log("Done get all users.")
        );
    }

    // getMyWorks() {
    //     this.work.allMyWorks().subscribe(
    //         works => {
    //             // console.log(works);
    //             this.works = works;
    //         },
    //         error => this.messageWorks = <any>error,
    //         () => console.log("Done get my works.")
    //     );
    // }
}
import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";

import { TAB_DIRECTIVES } from "ng2-tabs";

import { AuthService } from "../services/auth";
import { UserService } from "../services/user";
import { MessageService } from "../services/message";

import { UserModel } from "../models/user";
import { MessageModel } from "../models/message";

@Component({
    selector: "dashboard-component",
    templateUrl: "../../views/dashboard.html",
    directives: [TAB_DIRECTIVES]
})

export class Dashboard implements OnInit {

    private message: string;
    private messageUsers: string;
    private messageMessages: string;

    private usersList: UserModel[];
    private messagesList: MessageModel[];

    constructor(private authService: AuthService, private user: UserService, private router: Router,
        private messages: MessageService) {
        this.message = "My Dashboard in SAM";
    }

    ngOnInit() {
        this.getAllUsers();
        this.getAllMessages();
    }

    logout() {
        this.authService.logout();
        this.router.navigate(["/login"]);
    }

    getAllUsers() {
        this.user.all().subscribe(
            users => this.usersList = users,
            error => this.messageUsers = <any>error,
            () => console.log("Done get all users.")
        );
    }

    getAllMessages() {
        this.messages.read().subscribe(
            m => {
                this.messagesList = m;
                console.log(m);
            },
            error => this.messageMessages = <any>error,
            () => console.log("Done get all messages.")
        );
    }
}

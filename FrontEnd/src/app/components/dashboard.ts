import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";

import { AuthService } from "../services/auth";
import { UserService } from "../services/user";
import { MessageService } from "../services/message";
import { CategoriesService } from "../services/categories";
import { PaymentService } from "../services/payment";

import { UserModel } from "../models/user";
import { MessageModel } from "../models/message";
import { ProfileModel } from "../models/profile";
import { CategoryModel } from "../models/category";

@Component({
    selector: "dashboard-component",
    templateUrl: "../../views/dashboard.html"
})

export class Dashboard implements OnInit {

    private error: string;
    private errorUsers: string;
    private errorMessages: string;
    private messageCredit: string;
    private messageCode: string;
    private code: string;

    private usersList: UserModel[];
    private catList: CategoryModel[];
    private messagesList: MessageModel[];
    private model: ProfileModel = new ProfileModel();
    private modelProfileForm: ProfileModel;

    constructor(
        private authService: AuthService,
        private user: UserService,
        private router: Router,
        private messages: MessageService,
        private payment: PaymentService,
        private cat: CategoriesService
    ) { }

    ngOnInit() {
        this.getAllUsers();
        this.getCategories();
        this.getAllMessages();
        this.user.getMyProfile().subscribe(
            profile => {
                // console.log(this.model.activated)
                // if (this.model.activated === undefined)
                //     this.model.activated === false;
                this.model = profile;
                this.modelProfileForm = profile;
            },
            error => this.errorUsers = <any>error,
            () => console.log("Done get my profile.")
        );
    }

    logout() {
        this.authService.logout();
        this.router.navigate(["/login"]);
    }

    getAllUsers() {
        this.user.all().subscribe(
            users => this.usersList = users,
            error => this.errorUsers = <any>error,
            () => console.log("Done get all users.")
        );
    }

    getCategories() {
        this.cat.all().subscribe(
            c => this.catList = c,
            error => this.error = <any>error,
            () => console.log("Done get all categories dashboard.")
        );
    }

    getAllMessages() {
        this.messages.readAllLasts().subscribe(
            ml => {
                this.messagesList = ml;
                if (ml.length === 0)
                    this.errorMessages = "You have no messages yet.";
            },
            error => this.errorMessages = <any>error,
            () => console.log("Done get all messages.")
        );
    }

    addCredit() {
        if (this.model.addCredit < 1)
            this.messageCredit = "Credit must be more than zero to be added.";
        else {
            this.messageCredit = "Credit sent...";
            this.payment.addCredit(this.model.addCredit).subscribe(
                added => {
                    this.model.credit = added;
                    this.messageCredit = "Credit of " + this.model.addCredit + " euros added.";
                    this.model.addCredit = 0;
                },
                error => this.messageCredit = <any>error,
                () => console.log("Done adding credit.")
            );
        }
    }

    addCode(code: string) {
        if (code.length === 0)
            this.messageCode = "Code can't be empty.";
        else {
            this.messageCode = "Code sent...";
            this.user.addCode(code).subscribe(
                x => this.messageCode = "Code added. Now your account is validated. Enjoy :)",
                error => this.messageCode = <any>error,
                () => console.log("Done adding code.")
            );
        }
    }

    addCodeResend() {
        this.messageCode = "Code resent...";
        this.user.resendCode().subscribe(
            x => this.messageCode = "Code resent. Now you can activate it.",
            error => this.messageCode = <any>error,
            () => console.log("Done resending code.")
        );
    }
}

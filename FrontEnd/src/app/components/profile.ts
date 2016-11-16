import { Component, OnInit, OnDestroy } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { DomSanitizer } from "@angular/platform-browser";

import { UserService } from "../services/user";
import { MessageService } from "../services/message";
import { RatingService } from "../services/rating";
import { AuthService } from "../services/auth";
import { CategoriesService } from "../services/categories";
import { TranslateService } from "ng2-translate";

import { ProfileModel } from "../models/profile";
import { MessageModel } from "../models/message";
import { RatingModel } from "../models/rating";
import { CategoryModel } from "../models/category";
import { UserDefaultImage } from "../config/userdefaultimage";

declare var jQuery: any;

@Component({
    selector: "profile-component",
    templateUrl: "../../views/profile.html",
    styleUrls: ["../../styles/profile.css"]
})

export class Profile implements OnInit, OnDestroy {

    private sub: any;
    private defaultImage = UserDefaultImage.image;
    private itsMe: boolean;

    private message: string;

    private model: ProfileModel = <ProfileModel>{};
    private cats: CategoryModel[];

    constructor(
        private route: ActivatedRoute,
        private user: UserService,
        private sanitizer: DomSanitizer,
        private m: MessageService,
        private r: RatingService,
        private authService: AuthService,
        private cat: CategoriesService,
        private trans: TranslateService
    ) { }

    ngOnInit() {
        this.sub = this.route.params.subscribe(p => {
            let id = p["id"];
            this.user.getProfile(id).subscribe(
                profile => {
                    this.model = profile;
                    console.log(profile.image)
                    this.model.image = profile.image === undefined || profile.image === ""
                        ? this.defaultImage : profile.image;
                    this.cat.all().subscribe(
                        c => {
                            this.cats = c;
                            this.model.categoryName = this.cats.find(x => x.id == this.model.category).name;
                            if (this.authService.isLoggedIn()) {
                                this.user.getMyProfile().subscribe(
                                    my => this.itsMe = profile.email === my.email,
                                    error => this.message = <any>error,
                                    () => this.trans.get("DoneGetMyProfile")
                                        .subscribe((res: string) => console.log(res))
                                );
                                this.r.readProfileAuth(profile.url).subscribe(
                                    prof => {
                                        this.model.rating = prof.myrating;
                                        this.model.average = prof.average;
                                    },
                                    error => this.message = <any>error,
                                    () => this.trans.get("DoneGetRatingProfile")
                                        .subscribe((res: string) => console.log(res))
                                );
                            }
                            else {
                                this.itsMe = true; // for unlogged we use same logic as it were us.
                                this.r.readProfile(profile.url).subscribe(
                                    prof => this.model.average = prof.average,
                                    error => this.message = <any>error,
                                    () => this.trans.get("DoneGetRatingProfile")
                                        .subscribe((res: string) => console.log(res))
                                );
                            }
                        },
                        error => this.message = <any>error
                    );
                },
                error => this.message = <any>error,
                () => this.trans.get("DoneGetProfile").subscribe((res: string) => console.log(res))
            );
        });
    }

    ngOnDestroy() {
        this.sub.unsubscribe();
    }

    rate() {
        if (this.model.rating < 0 || this.model.rating > 5)
            this.trans.get("RatingCantBeMore").subscribe((res: string) => this.message = res);
        else {
            this.trans.get("RatingProfileSent").subscribe((res: string) => this.message = res);

            let model = new RatingModel();
            model.id = this.model.email;
            model.number = this.model.rating;

            this.r.add(model).subscribe(
                () => this.trans.get("RatingSent").subscribe((res: string) => this.message = res),
                (res) => this.message = res,
                () => this.trans.get("DoneSendRating").subscribe((res: string) => console.log(res))
            );
        }
    }

    sendMessage(messageText: string) {
        if (!messageText || !this.model.email || this.model.email === "undefined")
            this.trans.get("MessageNotValid").subscribe((res: string) => this.message = res);
        else if (messageText.length < 4 || messageText.length > 500)
            this.trans.get("MessageNoCharacters").subscribe((res: string) => this.message = res);
        else {
            this.trans.get("MessageProfileSent").subscribe((res: string) => this.message = res);

            let model = new MessageModel();
            model.to_email = this.model.email;
            model.url = this.model.url;
            model.text = messageText;

            this.m.add(model).subscribe(
                () => this.trans.get("MessageSent").subscribe((res: string) => this.message = res),
                (res) => this.message = res,
                () => {
                    jQuery("#SendMessageModal").modal("hide");
                    this.trans.get("DoneSendMessage").subscribe((res: string) => console.log(res));
                }
            );
        }
    }
}

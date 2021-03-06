import { Component, OnInit } from "@angular/core";
import { Settings } from "../config/settings";

import { CategoriesService } from "../services/categories";
import { CategoryModel } from "../models/category";
import { UserModel } from "../models/user";
import { UserService } from "../services/user";
import { TranslateService } from "ng2-translate";

declare let google: any;

@Component({
    selector: "home-component",
    templateUrl: "../../views/home.html",
    styleUrls: ["../../styles/app-root.css"]
})

export class Home implements OnInit {

    private users: Array<UserModel>;
    private catAll: Array<CategoryModel>;
    private message: string;
    private c: string; // just for the view auto complete object

    private lat: number;
    private lng: number;
    private tag: number;
    private radius: number = 5;

    constructor(
        private cat: CategoriesService,
        private user: UserService,
        private trans: TranslateService
    ) { }

    getAddress(place: Object) {
        let location = place["geometry"]["location"];
        this.lat = location.lat();
        this.lng = location.lng();
    }

    getCategories() {
        this.cat.all().subscribe(
            c => {
                this.catAll = c;
                this.tag = c[0].id;
            },
            error => this.message = <any>error,
            () => this.trans.get("DoneCat").subscribe((res: string) => console.log(res))
        );
    }

    onChangeTag(e: any): void {
        this.tag = e.item.id;
    }

    onChangeRadius(value: number) {
        this.radius = value;
    }

    ngOnInit() {
        let searchBox: any = document.getElementById("location");
        let options = {
            // return only geocoding results, rather than business results.
            types: ["geocode"], componentRestrictions: { country: Settings.search_country }
        };

        let autocomplete = new google.maps.places.Autocomplete(searchBox, options);
        autocomplete.addListener("place_changed", () => {
            this.getAddress(autocomplete.getPlace());
        });
        this.getCategories();
    }

    search() {
        if (!this.lat || !this.lng)
            this.trans.get("PleaseSelectCity").subscribe((res: string) => this.message = res);
        else {
            this.user.search(this.lat, this.lng, this.tag, this.radius).subscribe(
                (users) => {
                    this.users = users;
                    if (users.length === 0)
                        this.trans.get("UsersNotFound").subscribe((res: string) => this.message = res);
                    else
                        this.message = "";
                },
                error => this.message = <any>error,
                () => this.trans.get("DoneSearchCall").subscribe((res: string) => console.log(res))
            );
        }
    }
}

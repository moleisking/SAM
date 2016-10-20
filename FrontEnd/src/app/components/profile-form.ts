import { Component, OnInit, Input, ViewChild } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { Settings } from "../config/settings";

import { UserService } from "../services/user";
import { UserDefaultImage } from "../config/userdefaultimage";
import { ProfileModel } from "../models/profile";
import { CategoryModel } from "../models/category";
import { TagModel } from "../models/tag";
import { SelectComponent } from "ng2-select/ng2-select";

declare let google: any;

@Component({
    selector: "profile-form-component",
    templateUrl: "../../views/profile-form.html",
    styleUrls: ["../../styles/form.css", "../../styles/ng2-select.css"]
})

export class ProfileFormComponent implements OnInit {

    @Input() model: ProfileModel;
    @Input() cats: CategoryModel[];
    @ViewChild(SelectComponent)
    private mySelect: SelectComponent;

    private tags: TagModel[];
    private tagsActive: TagModel[] = [];
    private tagsValue: any = [];
    private areTagsAvailable: boolean = false;
    private image: string;

    private myForm: FormGroup;
    private defaultImage = UserDefaultImage.image;
    private message: string;

    constructor(
        private user: UserService,
        private formBuilder: FormBuilder
    ) { this.message = "Profile form messages will be here."; }

    ngOnInit() {
        let regexPatterns = { numbers: "^[0-9]*$" };
        this.myForm = this.formBuilder.group({
            description: [this.model.description, Validators.required],
            address: [this.model.address, Validators.required],
            mobile: [this.model.mobile, Validators.required],
            dayRate: [this.model.dayRate],
            hourRate: [this.model.hourRate],
            curLat: [this.model.curLat],
            curLng: [this.model.curLng],
            category: [this.model.category,
            Validators.compose([Validators.pattern(regexPatterns.numbers), Validators.required])],
            tags: [this.model.tags],
            looking: [this.model.looking]
        });

        this.tags = this.cats.find(x => x.id === this.model.category).tags;
        this.model.tags.split(",").forEach(element => {
            if (element)
                this.tagsActive.push(this.cats.find(e => e.id === this.model.category).tags
                    .find(e => e.id === +element));
        });
        this.tagsValue = this.model.tags;
        this.areTagsAvailable = this.tags.length > 0;

        this.image = this.model.image ? this.model.image : this.defaultImage;

        this.googleHook();
    }

    googleHook() {
        let searchBox: any = document.getElementById("address");
        let options = {
            types: ["geocode"], // return only geocoding results, rather than business results.
            componentRestrictions: { country: Settings.search_country }
        };

        let autocomplete = new google.maps.places.Autocomplete(searchBox, options);

        // Add listener to the place changed event
        autocomplete.addListener("place_changed", () => {
            this.getAddress(autocomplete.getPlace());
        });
    }

    getAddress(place: Object) {
        let address = place["formatted_address"];
        let location = place["geometry"]["location"];
        this.myForm.patchValue({ curLat: location.lat() });
        this.myForm.patchValue({ curLng: location.lng() });
        this.myForm.patchValue({ address: address });
        console.log("place", address, location, location.lat(), location.lng());
    }

    ngAfterViewChecked() {
        if (document.getElementById("image") !== null)
            document.getElementById("image")
                .addEventListener("change", e => { this.readImage(e); }, false);
    }

    readImage(e: any) {
        let fileName = e.target.files[0];
        if (!fileName)
            return;
        let reader = new FileReader();
        let self = this;
        reader.onload = file => {
            let contents: any = file.target;
            self.image = contents.result;
        };
        reader.readAsDataURL(fileName);
    }

    onChangeCategory(value: number) {
        this.tagsValue = [];
        this.tagsActive = [];
        this.tags = this.cats.find(x => x.id === value).tags;
        this.areTagsAvailable = this.tags.length > 0;
        this.mySelect.active = [];
    }

    refreshValue(value: number): void {
        this.tagsValue = value;
    }

    save() {
        if (!this.myForm.dirty && !this.myForm.valid)
            this.message = "Form not valid to be sent.";
        else {
            this.myForm.controls["tags"].setValue(this.tagsValue.map((item: any) => { return item.id; }).join(","));
            this.message = "User profile sent...";
            this.user.saveProfile(this.myForm.value, this.image).subscribe(
                () => {
                    this.message = "User profile saved.";
                },
                (res) => {
                    this.message = res;
                }
            );
        }
    }
}

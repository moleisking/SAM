import { Component, OnInit } from "@angular/core";

import { Settings } from "./config/settings";
import { AuthService } from "./services/auth";
import { TranslateService } from "ng2-translate";

@Component({
  selector: "app-root",
  templateUrl: "../views/app-root.html",
  styleUrls: ["../styles/app-root.css"],
})

export class AppComponent implements OnInit {
  public supportedLangs: any[];

  constructor(
    private authService: AuthService,
    private trans: TranslateService
  ) { }

  ngOnInit() {
    // console.log(navigator.language);
    // console.log(this.trans.currentLoader.prefix);
    this.supportedLangs = [
      { display: "English", value: "en" },
      { display: "Español", value: "es" }
    ];
    this.selectLang(null, Settings.localization);
  }

  isDisabledIfLoggedIn() {
    return this.authService.isLoggedIn();
  }

  selectLang(event: any, lang: string) {
    if (event !== null)
      event.preventDefault();
    // this language will be used as a fallback when a translation isn't found in the current language
    this.trans.setDefaultLang("en");
    // the lang to use, if the lang isn't available, it will use the current loader to get them
    this.trans.use(lang);
    // this.refreshText();
  }

  isCurrentLang(lang: string) {
    return lang === this.trans.currentLang;
  }
}

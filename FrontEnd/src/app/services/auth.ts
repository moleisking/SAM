import { Injectable } from "@angular/core";
import { Http, Headers, Response, RequestOptions } from "@angular/http";
import { Settings } from "../config/settings";
import { Observable } from "rxjs/Rx";

@Injectable()

export class AuthService {

  constructor(private http: Http) { }

  login(usercreds:any) {
    let body = "name=" + usercreds.name + "&pass=" + usercreds.pass;
    let headers = new Headers();
    headers.append("Content-Type", "application/X-www-form-urlencoded");
    let options = new RequestOptions({ headers: headers });

    return this.http.post(Settings.backend_url + "/authenticate", body, options)
      .map(this.extractData).catch(this.handleError);
  }

  logout() {
    localStorage.removeItem("auth_key");
  }

  isLoggedIn() {
    if (localStorage.getItem("auth_key") !== null)
      return true;
  }

  private extractData(res: Response) {
    // console.log(res);
    localStorage.setItem("auth_key", res.json().token.split(" ")[1]);
    let body = res.json();
    return body.data || {};
  }

  private handleError(error: any) {
    // console.error(error);
    let errMsg = (error.message) ? error.message :
      (error._body) ? error._body :
        error.status ? `${error.status} - ${error.statusText}` : "Server error";
    console.error(errMsg);
    return Observable.throw(errMsg);
  }
}
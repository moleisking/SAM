import { Injectable } from "@angular/core";
import { Http, Headers, Response, RequestOptions } from "@angular/http";
import { Settings } from "../config/settings";
import { User } from "../models/user";
import { Profile } from "../models/profile";
import { Observable } from "rxjs/Rx";

@Injectable()

export class UserService {

  constructor(private http: Http) { }

  register(usercreds): Observable<any> {
    let body = "name=" + usercreds.name + "&pass=" + usercreds.pass + "&email=" + usercreds.email;
    let headers = new Headers();
    headers.append("Content-Type", "application/X-www-form-urlencoded");
    let options = new RequestOptions({ headers: headers });

    return this.http.post(Settings.backend_url + "/api/signup", body, options).catch(this.handleError);
  }

  saveProfile(profileform): Observable<any> {
    let headers = new Headers();
    headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));
    headers.append("Content-Type", "application/X-www-form-urlencoded");
    let c = "description=" + profileform.description + "&name=" + profileform.name;

    return this.http.post(Settings.backend_url + "/users/saveprofile", c, { headers: headers }).catch(this.handleError);
  }

  all(): Observable<User[]> {
    let headers = new Headers();
    headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));

    return this.http.get(Settings.backend_url + "/users", { headers: headers })
      .map(this.extractData).catch(this.handleError);
  }

  getLoggedProfile(): Observable<Profile> {
    let headers = new Headers();
    headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));

    return this.http.get(Settings.backend_url + "/users/getprofile", { headers: headers })
      .map(this.extractData)
      .catch(this.handleError);
  }

  getProfile(id): Observable<Profile> {
    let headers = new Headers();
    let creds = "name=" + id;
    headers.append("Content-Type", "application/X-www-form-urlencoded");
    headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));

    return this.http.post(Settings.backend_url + "/users/getprofile", creds, { headers: headers })
      .map(this.extractData).catch(this.handleError);
  }

  forgottenpassword(form): Observable<any> {
    let creds = "email=" + form.email;
    let headers = new Headers();
    headers.append("Content-Type", "application/X-www-form-urlencoded");

    return this.http.post(Settings.backend_url + "/api/forgottenpassword", creds, { headers: headers })
      .map(this.extractData).catch(this.handleError);
  }

  private extractData(res: Response) {
    // console.log(res);
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
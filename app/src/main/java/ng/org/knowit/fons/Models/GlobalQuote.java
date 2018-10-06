package ng.org.knowit.fons.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GlobalQuote {

    @SerializedName("Global Quote")
    @Expose
    private CompanyQuote companyQuote;

    public CompanyQuote getCompanyQuote() {
        return companyQuote;
    }

    public void setCompanyQuote(CompanyQuote companyQuote) {
        this.companyQuote = companyQuote;
    }
}

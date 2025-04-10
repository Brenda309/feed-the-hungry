

package com.brenda.FeedTheHungry.mail;

import com.brenda.FeedTheHungry.appuser.AppUserRole;

public class EmailBuilder {
    
    public static String buildEmailContent(String name, String link, AppUserRole role) {
        switch (role) {
            case DONOR:
                return buildDonorEmail(name, link);
            case BENEFICIARY:
                return buildBeneficiaryEmail(name, link);
            case VOLUNTEER:
                return buildVolunteerEmail(name, link);
            case ADMIN:
                return buildAdminEmail(name);
            default:
                return buildDefaultEmail(name, link);
        }
    }

    public static String getEmailSubject(AppUserRole role) {
        switch (role) {
            case DONOR:
                return "Thank you for registering as a donor!";
            case BENEFICIARY:
                return "Welcome to FeedTheHungry - Next Steps";
            case VOLUNTEER:
                return "Volunteer Registration Confirmation";
            case ADMIN:
                return "Admin Account Created";
            default:
                return "Confirm your email";
        }
    }

    private static String buildDonorEmail(String name, String link) {
        return String.format("""
            <div style="font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c">
                <p style="font-size:19px;line-height:25px;color:#0b0c0c">Dear %s,</p>
                <p>Thank you for choosing to be a donor with FeedTheHungry!</p>
                <p>Your contributions will make a direct impact in fighting hunger.</p>
                <p>Please confirm your email to complete registration:</p>
                <p><a href="%s">Confirm Email</a></p>
                <p>After confirmation, you can:</p>
                <ul>
                    <li>Schedule food donations</li>
                    <li>Track your impact</li>
                    <li>Receive tax receipts</li>
                </ul>
                <p>Thank you for your generosity!</p>
            </div>
            """, name, link);
    }

    private static String buildBeneficiaryEmail(String name, String link) {
        return String.format("""
            <div style="font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c">
                <p style="font-size:19px;line-height:25px;color:#0b0c0c">Dear %s,</p>
                <p>Welcome to FeedTheHungry's assistance program!</p>
                <p>Please confirm your email to continue:</p>
                <p><a href="%s">Confirm Email</a></p>
                <p>After confirmation, you can:</p>
                <ul>
                    <li>Apply for food assistance</li>
                    <li>Check distribution schedules</li>
                    <li>Update your information</li>
                </ul>
                <p>We're here to help you through this process.</p>
            </div>
            """, name, link);
    }

    private static String buildVolunteerEmail(String name, String link) {
        return String.format("""
            <div style="font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c">
                <p style="font-size:19px;line-height:25px;color:#0b0c0c">Dear %s,</p>
                <p>Thank you for volunteering with FeedTheHungry!</p>
                <p>Please confirm your email to complete registration:</p>
                <p><a href="%s">Confirm Email</a></p>
                <p>After confirmation, you'll receive:</p>
                <ul>
                    <li>Volunteer orientation materials</li>
                    <li>Schedule of upcoming opportunities</li>
                    <li>Your assigned coordinator's contact</li>
                </ul>
                <p>We appreciate your time and dedication!</p>
            </div>
            """, name, link);
    }

    private static String buildAdminEmail(String name) {
        return String.format("""
            <div style="font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c">
                <p style="font-size:19px;line-height:25px;color:#0b0c0c">Hello %s,</p>
                <p>Your admin account has been successfully created.</p>
                <p>You can now access the admin dashboard to:</p>
                <ul>
                    <li>Manage users and permissions</li>
                    <li>Monitor donations and distributions</li>
                    <li>Generate reports</li>
                    <li>Coordinate volunteers</li>
                </ul>
                <p>Login at: <a href="http://localhost:8080/admin">Admin Portal</a></p>
                <p>Thank you for your leadership!</p>
            </div>
            """, name);
    }

    private static String buildDefaultEmail(String name, String link) {
        return String.format("""
            <div style="font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c">
                <p style="font-size:19px;line-height:25px;color:#0b0c0c">Hi %s,</p>
                <p>Thank you for registering with FeedTheHungry!</p>
                <p>Please confirm your email by clicking below:</p>
                <p><a href="%s">Confirm Email</a></p>
                <p>Link expires in 15 minutes.</p>
            </div>
            """, name, link);
    }
}

"use client";

import Button, { ButtonVariants } from "@/components/button";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./index.module.css";

interface TermsScreenProps {}

const TermsScreen: React.FC<TermsScreenProps> = ({}) => {
  const { t } = useTranslation();
  return (
    <div className={styles.container}>
      <div>
        <Button href="/" variant={ButtonVariants.filled}>
          {t("termsPageGoBack")}
        </Button>
      </div>
      <div>
        <div
          style={{
            marginTop: 5,
            fontWeight: 700,
            textAlign: "justify",
            fontSize: 22,
          }}
        >
          Privacy Policy, Terms & Conditions
        </div>
        <div
          style={{
            marginTop: 5,
            fontWeight: 500,
            textAlign: "justify",
            fontSize: 14,
          }}
        >
          <p>
            Çağdaş Tunca built the Jinear app as a Freemium app. This SERVICE is provided by Çağdaş Tunca at no cost and is
            intended for use as is.
          </p>{" "}
          <p>
            This page is used to inform visitors regarding our policies with the collection, use, and disclosure of Personal
            Information if anyone decided to use our Service.
          </p>
          <p>
            If you choose to use our Service, then you agree to the collection and use of information in relation to this policy.
            The Personal Information that we collect is used for providing and improving the Service. We will not use or share
            your information with anyone except as described in this Privacy Policy.
          </p>
          <p>
            The terms used in this Privacy Policy have the same meanings as in our Terms and Conditions, which is accessible at
            Jinear unless otherwise defined in this Privacy Policy.
          </p>

          <br/>

          <p>
            <strong>Information Collection and Use</strong>
          </p>{" "}
          <p>
            For a better experience, while using our Service, We may require you to provide us with certain personally
            identifiable information, including but not limited to e-mail, phone number, gps data, message data, birthdate, gender
            and sexual interests. The information that we request could be retained on your device and our servers.
          </p>
          <p>The app does use third party services that may collect information used to identify you.</p>
          <p>Link to privacy policy of third party service providers used by the app</p>
          <ul>
            <li>
              <a href="https://www.google.com/policies/privacy/" target="_blank" rel="noopener noreferrer">
                Google Play Services
              </a>
            </li>
            <li>
              <a href="https://firebase.google.com/policies/analytics" target="_blank" rel="noopener noreferrer">
                Firebase Analytics
              </a>
            </li>
            <li>
              <a href="http://try.crashlytics.com/terms/privacy-policy.pdf" target="_blank" rel="noopener noreferrer">
                Crashlytics
              </a>
            </li>
            <li>
              <a href="https://www.apple.com/privacy/" target="_blank" rel="noopener noreferrer">
                Apple
              </a>
            </li>
            <li>
              <a href="https://sentry.io/privacy/" target="_blank" rel="noopener noreferrer">
                Sentry
              </a>
            </li>
          </ul>

          <br/>

          <p>
            <strong>Log Data</strong>
          </p>
          <p>
            We want to inform you that whenever you use our Services, in a case of an error in the app we collect data and
            information (could be internally or could be through third party products) on your phone called Log Data. This Log
            Data may include information such as your device Internet Protocol (“IP”) address, device name, operating system
            version, the configuration of the app when utilizing our Service, the time and date of your use of the Service, and
            other statistics.
          </p>

          <br/>

          <p>
            <strong>Service Providers</strong>
          </p>{" "}
          <p>We may employ third-party companies and individuals due to the following reasons:</p>
          <ul>
            <li>To facilitate our Service;</li>
            <li>To provide the Service on our behalf;</li>
            <li>To perform Service-related services; or</li>
            <li>To assist us in analyzing how our Service is used.</li>
          </ul>
          <p>
            We want to inform users of this Service that these third parties have access to your Personal Information. The reason
            is to perform the tasks assigned to them on our behalf. However, they are obligated not to disclose or use the
            information for any other purpose.
          </p>

          <br/>

          <p>
            <strong>Security</strong>
          </p>{" "}
          <p>
            We value your trust in providing us your Personal Information, thus we are striving to use commercially acceptable
            means of protecting it. But remember that no method of transmission over the internet, or method of electronic storage
            is 100% secure and reliable, and We cannot guarantee its absolute security.
          </p>

          <br/>

          <p>
            <strong>Data Protection Mechanisms For Sensitive Data</strong>
          </p>{" "}
          <p>
            We are committed to protecting the privacy and security of your sensitive data. To ensure the confidentiality, integrity,
            and availability of this data, we have implemented the following data protection mechanisms:
            <br/>
            <ul>
              <li>
                <b>Encryption</b>
                <br/>
                <p>
                  All sensitive data transmitted between your device and our servers is encrypted using industry-standard encryption protocols. This ensures that even if intercepted, the data remains unreadable without the appropriate decryption key.
                </p>
              </li>
              <li>
                <b>Access Control</b>
                <br/>
                <p>
                  Access to sensitive data is restricted to authorized personnel only. We employ strong authentication measures, such as passwords and multi-factor authentication, to prevent unauthorized access.
                </p>
              </li>
              <li>
                <b>Data Minimization</b>
                <br/>
                <p>
                  We only collect and process sensitive data that is necessary for the purposes outlined in our privacy policy. We do not retain this data longer than necessary for its intended use.
                </p>
              </li>
              <li>
                <b>Anonymization and Pseudonymization</b>
                <br/>
                <p>
                  Where possible, we anonymize or pseudonymize sensitive data to prevent identification of individuals. This helps mitigate the risk of data breaches and unauthorized access.
                </p>
              </li>
              <li>
                <b>Regular Audits and Monitoring</b>
                <br/>
                <p>
                  We conduct regular audits of our data protection mechanisms and security practices to ensure compliance with industry standards and legal requirements. Additionally, we continuously monitor for any unauthorized access or suspicious activities.
                </p>
              </li>
              <li>
                <b>Data Breach Response Plan</b>
                <br/>
                <p>
                  In the event of a data breach involving sensitive data, we have a comprehensive response plan in place. This includes immediate containment, notification of affected individuals and authorities as required by law, and remedial actions to prevent future breaches.
                </p>
              </li>
              <li>
                <b>User Education</b>
                <br/>
                <p>
                  We provide resources and guidance to our users on best practices for protecting their sensitive data, such as using strong passwords, avoiding phishing attempts, and being cautious with sharing personal information.
                </p>
              </li>
            </ul>
            <p>
              By implementing these data protection mechanisms, we strive to maintain the highest standards of security and privacy for your sensitive data.
            </p>
          </p>

          <br/>

          <p>
            <strong>Data Retention and/or Deletion Disclosures</strong>
          </p>{" "}
          <p>
            We are committed to transparently disclosing our data retention and deletion practices to ensure the protection of your privacy. Below are the key points regarding how we handle the retention and deletion of data:
            <br/>
            <ul>
              <li>
                <b>Retention Period</b>
                <br/>
                <p>
                  We retain personal data only for as long as necessary to fulfill the purposes outlined in our privacy policy, unless a longer retention period is required or permitted by law. The specific retention periods may vary depending on the type of data and the applicable legal requirements.
                </p>
              </li>
              <li>
                <b>Purpose Limitation</b>
                <br/>
                <p>
                  We only retain personal data for the purposes for which it was collected and authorized by you. If we intend to use the data for any other purpose, we will obtain your explicit consent unless the new purpose is compatible with the original purpose.
                </p>
              </li>
              <li>
                <b>Data Deletion</b>
                <br/>
                <p>
                  When personal data is no longer needed for its intended purpose or when you withdraw your consent (where applicable), we will securely delete or anonymize the data to prevent unauthorized access or use. This includes data stored in our systems, backups, and any third-party services we utilize for data processing.
                </p>
              </li>
              <li>
                <b>User Requests</b>
                <br/>
                <p>
                  You have the right to request the deletion of your personal data in certain circumstances, such as when the data is no longer necessary for the purposes for which it was collected, or when you withdraw your consent. To exercise this right, please contact us using the contact information provided in this privacy policy.
                </p>
              </li>
              <li>
                <b>Data Retention for Legal Compliance</b>
                <br/>
                <p>
                  In some cases, we may be required to retain personal data for a longer period to comply with legal obligations, resolve disputes, enforce our agreements, or protect our legal rights. However, we will ensure that such data is only retained to the extent necessary for these purposes and will implement appropriate safeguards to protect your privacy.
                </p>
              </li>
              <li>
                <b>Backup Retention</b>
                <br/>
                <p>
                  We may retain backup copies of your data for a limited period to protect against data loss due to technical failures or disasters. These backups are securely stored and are subject to the same data protection measures as our live data.
                </p>
              </li>
            </ul>
            <p>
              By disclosing our data retention and deletion practices, we aim to provide transparency and clarity regarding how we handle your personal data throughout its lifecycle.
            </p>
          </p>

          <br/>


          <p>
            <strong>Children’s Privacy</strong>
          </p>{" "}
          <p>
            These Services do not address anyone under the age of 18. We do not knowingly collect personally identifiable
            information from children under 18. In the case We discover that a child under 18 has provided us with personal
            information, We immediately delete this from our servers. If you are a parent or guardian and you are aware that your
            child has provided us with personal information, please contact us so that we will be able to do necessary actions.
          </p>

          <br/>

          <p>
            <strong>Limited Use Policy (Google) </strong>
          </p>{" "}
          <p>
            Jinear’s use and transfer to any other app of information received from Google APIs will adhere to
            <a href="https://developers.google.com/terms/api-services-user-data-policy#additional_requirements_for_specific_api_scopes" target="_blank" rel="noopener noreferrer"> Google API Services User Data Policy</a>,
            including the Limited Use requirements.
          </p>

          <br/>

          <p>
            <strong>Changes to This Privacy Policy</strong>
          </p>{" "}
          <p>
            We may update our Privacy Policy from time to time. Thus, you are advised to review this page periodically for any
            changes. We may or may not notify you of any changes by posting the new Privacy Policy on this page. These changes are
            effective immediately after they are posted on this page.
          </p>

          <br/>

          <p>
            <strong>Contact Us</strong>
          </p>{" "}
          <p>
            If you have any questions or suggestions about our Terms and Privacy Policy, do not hesitate to contact us at{" "}
            <a href="mailto://cgdstnc@gmail.com" target="_blank" rel="noopener noreferrer">
              cgdstnc@gmail.com
            </a>
            .
          </p>

          <br/>

          <p>
            <strong>Refunds & Cancellations</strong>
          </p>{" "}
          <p>
            When cancelling a monthly subscription, all future charges associated with future months of your subscription will be
            cancelled. You may notify us of your intent to cancel at any time; your cancellation will become effective at the end
            of your current monthly billing period. You will not receive a refund; however your subscription access and/or
            delivery and accompanying subscriber benefits will continue for the remainder of the current monthly billing period.
            <br />
            When cancelling an annual subscription, all future charges associated with future years of your subscription will be
            cancelled. You may notify us of your intent to cancel at any time; your cancellation will become effective at the end
            of your current annual billing period. You will not receive a refund, prorated or otherwise, for the remainder of the
            annual term. However, your subscription access and/or delivery and accompanying subscriber benefits will continue for
            the remainder of the current annual billing period.
            <br />
            In order to cancel your subscription please
            <ul>
              <li>
                <a href="/login" target="_blank" rel="noopener">
                  Log In
                </a>{" "}
                to your Account.
              </li>
              <li>Navigate to the workspace you want to cancel it&apos;s subscription.</li>
              <li>Go to workspace settings by clicking the workspace name from the side menu</li>
              <li>There you can cancel or update payment information</li>
            </ul>
            <br />
            For refunds and any other issue please contact us at{" "}
            <a href="mailto://cgdstnc@gmail.com" target="_blank" rel="noopener noreferrer">
              cgdstnc@gmail.com
            </a>
          </p>
        </div>
      </div>
      <br/>
      <div>
        <Button href="/" variant={ButtonVariants.filled}>
          {t("termsPageGoBack")}
        </Button>
      </div>
    </div>
  );
};

export default TermsScreen;

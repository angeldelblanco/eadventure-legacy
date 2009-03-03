package es.eucm.eadventure.common.data.assessment;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores an assessment profile. Each profile contains the path of the xml file where it is stored (relative to the zip file), along with the
 * list of assessment rules defined in the profile
 * @author Javier
 *
 */
public class AssessmentProfile implements Cloneable {

	/**
	 * Relative path of the file containing the asssessment rules
	 */
	private String path;
	
	/**
	 * The list of assessment rules
	 */
	private List<AssessmentRule> rules;
	
	/**
	 * List of referenced flags
	 */
	private List<String> flags;
	
	/**
	 * List of referenced vars
	 */
	private List<String> vars;
	
	/**
	 * Store if adaptation profile is for scorm2004 
	 */
	private boolean scorm2004;
	
	
	/**
	 * Store if adaptation profile is for scorm 1.2
	 */
	private boolean scorm12;
	
	////////FEEDBACK
	/**
	 * If true, the assessment report is shown to the student at the end of the chapter
	 */
	private boolean showReportAtEnd;
	
	/**
	 * If true, the student will be asked to send an email with the report
	 */
	private boolean sendByEmail;
	
	/**
	 * The email where the student's report must be sent
	 */
	private String email;
	
	private boolean smtpSSL;
	
	private String smtpServer;
	
	private String smtpPort;
	
	private String smtpUser;
	
	private String smtpPwd;
	
	/**
	 * Empty constructor 
	 */
	public AssessmentProfile() {
		this( new ArrayList<AssessmentRule>(), null );
		
	}
	
	/**
	 * @param path
	 */
	public AssessmentProfile(String path) {
		this( new ArrayList<AssessmentRule>(), path );
	}

	public AssessmentProfile(List<AssessmentRule> assessmentRules, String path2) {
		rules = assessmentRules;
		this.path = path2;
		flags = new ArrayList<String>();
		vars = new ArrayList<String>();
		sendByEmail = false;
		email = "";
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the rules
	 */
	public List<AssessmentRule> getRules() {
		return rules;
	}

	/**
	 * Adds a new rule to the structure
	 */
	public void addRule ( AssessmentRule rule ){
		this.rules.add(rule);
	}

	/**
	 * Set all the rules
	 * @param assessmentRules
	 */
	public void setRules(List<AssessmentRule> assessmentRules) {
		this.rules = assessmentRules;
	}
	
	/**
	 * Adds a new flag
	 * @param flag
	 */
	public void addFlag( String flag ){
		if (!flags.contains(flag)){
			flags.add(flag);
		}
	}
	
	/**
	 * Adds a new var
	 * @param var
	 */
	public void addVar( String var ){
		if (!vars.contains(var)){
			vars.add(var);
		}
	}

	/**
	 * @return the flags
	 */
	public List<String> getFlags() {
		return flags;
	}

	/**
	 * @return the vars
	 */
	public List<String> getVars() {
		return vars;
	}

	/**
	 * @param flags the flags to set
	 */
	public void setFlags(List<String> flags) {
		this.flags = flags;
	}

	/**
	 * @param vars the vars to set
	 */
	public void setVars(List<String> vars) {
		this.vars = vars;
	}

	/**
	 * @return the showReportAtEnd
	 */
	public boolean isShowReportAtEnd() {
		return showReportAtEnd;
	}

	/**
	 * @param showReportAtEnd the showReportAtEnd to set
	 */
	public void setShowReportAtEnd(boolean showReportAtEnd) {
		this.showReportAtEnd = showReportAtEnd;
	}

	/**
	 * @return the sendByEmail
	 */
	public boolean isSendByEmail() {
		return sendByEmail;
	}

	/**
	 * @param sendByEmail the sendByEmail to set
	 */
	public void setSendByEmail(boolean sendByEmail) {
		this.sendByEmail = sendByEmail;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	public void setSmtpSSL(boolean equals) {
		smtpSSL = equals;
	}

	public void setSmtpPort(String value) {
		smtpPort = value;
	}

	public void setSmtpUser(String value) {
		smtpUser = value;
	}

	public void setSmtpPwd(String value) {
		smtpPwd = value;
	}

	/**
	 * @return the smtpSSL
	 */
	public boolean isSmtpSSL() {
		return smtpSSL;
	}

	/**
	 * @return the smtpPort
	 */
	public String getSmtpPort() {
		return smtpPort;
	}

	/**
	 * @return the smtpUser
	 */
	public String getSmtpUser() {
		return smtpUser;
	}

	/**
	 * @return the smtpPwd
	 */
	public String getSmtpPwd() {
		return smtpPwd;
	}

	public void setSmtpServer(String value) {
		this.smtpServer = value;
	}

	public String getSmtpServer() {
		return smtpServer;
	}
	
	
	public Object clone() throws CloneNotSupportedException {
		AssessmentProfile ap = (AssessmentProfile) super.clone();
		ap.email = (email != null ? new String(email) : null);
		if (flags != null) {
			ap.flags = new ArrayList<String>();
			for (String s : flags)
				ap.flags.add((s != null ? new String(s) : null));
		}
		ap.path = (path != null ? new String(path) : null);
		if (rules != null) {
			ap.rules = new ArrayList<AssessmentRule>();
			for (AssessmentRule ar : rules)
				ap.rules.add((AssessmentRule) ar.clone());
		}
		ap.sendByEmail = sendByEmail;
		ap.showReportAtEnd = showReportAtEnd;
		ap.smtpPort = (smtpPort != null ? new String(smtpPort) : null);
		ap.smtpPwd = (smtpPwd != null ? new String(smtpPwd) : null);
		ap.smtpServer = (smtpServer != null ? new String(smtpServer) : null);
		ap.smtpSSL = smtpSSL;
		ap.smtpUser = (smtpUser != null ? new String(smtpUser) : null);
		if (vars != null) {
			ap.vars = new ArrayList<String>();
			for (String s : vars) {
				ap.vars.add((s != null ? new String(s) : null));
			}
		}
		return ap;
	}

	/**
	 * @return the scorm2004
	 */
	public boolean isScorm2004() {
		return scorm2004;
	}

	/**
	 * @param scorm2004 the scorm2004 to set
	 */
	public void setScorm2004(boolean scorm2004) {
		this.scorm2004 = scorm2004;
	}

	/**
	 * @return the scorm12
	 */
	public boolean isScorm12() {
		return scorm12;
	}

	/**
	 * @param scorm12 the scorm12 to set
	 */
	public void setScorm12(boolean scorm12) {
		this.scorm12 = scorm12;
	}
}

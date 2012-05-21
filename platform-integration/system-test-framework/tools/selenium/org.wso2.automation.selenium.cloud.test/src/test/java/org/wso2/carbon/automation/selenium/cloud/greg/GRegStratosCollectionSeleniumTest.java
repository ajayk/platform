/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package org.wso2.carbon.automation.selenium.cloud.greg;

import com.thoughtworks.selenium.Selenium;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.*;
import org.wso2.platform.test.core.BrowserManager;
import org.wso2.platform.test.core.ProductConstant;
import org.wso2.platform.test.core.utils.UserInfo;
import org.wso2.platform.test.core.utils.UserListCsvReader;
import org.wso2.platform.test.core.utils.environmentutils.ProductUrlGeneratorUtil;
import org.wso2.platform.test.core.utils.seleniumutils.GRegSeleniumUtils;
import org.wso2.platform.test.core.utils.seleniumutils.StratosUserLogin;

import java.net.MalformedURLException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


public class GRegStratosCollectionSeleniumTest {
    private static final Log log = LogFactory.getLog(GRegStratosCollectionSeleniumTest.class);
    private static Selenium selenium;
    private static WebDriver driver;
    String productName = "greg";
    String userName;
    String password;

    @BeforeClass(alwaysRun = true)
    public void init() throws MalformedURLException, InterruptedException {
        UserInfo userDetails = UserListCsvReader.getUserInfo(3);
        userName = userDetails.getUserName();
        password = userDetails.getPassword();
        String baseUrl = new ProductUrlGeneratorUtil().getServiceHomeURL(ProductConstant.GREG_SERVER_NAME);
        log.info("baseURL is :" + baseUrl);
        driver = BrowserManager.getWebDriver();
        selenium = new WebDriverBackedSelenium(driver, baseUrl);
        driver.get(baseUrl);
    }

    @Test(groups = {"wso2.greg"}, description = "add a collection tree ", priority = 1)
    public void testAddCollectionTree() throws Exception {
        String collectionPath = "/selenium_root/collection_root/c1/c2";
        try {
            StratosUserLogin.userLogin(driver, selenium, userName, password, productName);
            gotoDetailViewTab();
            GRegSeleniumUtils.deleteResourceFromBrowser(driver, "selenium_root");
            addCollection(collectionPath);
            assertEquals(collectionPath, selenium.getValue("//input"),
                         "New Created Collection does not Exists :");
            userLogout();
            log.info("********GReg Stratos Add New Collection Test - Passed ***********");
        } catch (AssertionError e) {
            log.info("Add New Collection Test Failed :" + e);
            userLogout();
            throw new AssertionError("Add New Collection Test Failed :" + e);
        } catch (WebDriverException e) {
            log.info("Add New Collection Test Failed :" + e);
            userLogout();
            throw new WebDriverException("Add New Collection Test Failed :" + e);
        } catch (Exception e) {
            log.info("Add New Collection Test Failed :" + e);
            userLogout();
            throw new Exception("Add New Collection Test Failed :" + e);
        }
    }

    @Test(groups = {"wso2.greg"}, description = "add a comment to collection ", priority = 2)
    public void testAddComment() throws Exception {
        String collectionPath = "/selenium_root/collection_root/comment/a1";
        String comment = "Collection Comment1";

        try {
            StratosUserLogin.userLogin(driver, selenium, userName, password, productName);
            gotoDetailViewTab();
            GRegSeleniumUtils.deleteResourceFromBrowser(driver, "selenium_root");
            addCollection(collectionPath);
            assertEquals(collectionPath, selenium.getValue("//input"),
                         "New Created Collection does not Exists :");

            addComment(comment);
            deleteComment();
            userLogout();
            log.info("********GReg Stratos Add a Comment to Collection Test - Passed ***********");
        } catch (AssertionError e) {
            log.info("Add a Comment to Collection Test Failed :" + e);
            userLogout();
            throw new AssertionError("Add a Comment to Collection Test Failed :" +
                                           e);
        } catch (WebDriverException e) {
            log.info("Add a Comment to Collection Test Failed :" + e);
            userLogout();
            throw new WebDriverException("Add a Comment to Collection Test Failed :" +
                                         e);
        } catch (Exception e) {
            log.info("Add a Comment to Collection Test Failed :" + e);
            userLogout();
            throw new Exception("Add a Comment to Collection Test Failed :" + e);
        }
    }


    @Test(groups = {"wso2.greg"}, description = "apply a tag to a collection", priority = 3)
    public void testAddTagToCollection() throws Exception {
        String collectionPath = "/selenium_root/collection_root/tag/a1";
        String tag = "Collection_tag1";

        try {
            StratosUserLogin.userLogin(driver, selenium, userName, password, productName);
            gotoDetailViewTab();
            GRegSeleniumUtils.deleteResourceFromBrowser(driver, "selenium_root");
            addCollection(collectionPath);      //Create Collection  1
            assertEquals(collectionPath, selenium.getValue("//input"),
                         "New Created Collection does not Exists :");
            applyTag(tag);

            Actions builder = new Actions(driver);
            WebElement tagElement = driver.findElement(By.xpath("//tr/td[4]/div[12]/div/div[12]/div[3]/a"));
            builder.moveToElement(tagElement).build().perform();

            deleteTag();
            userLogout();
            log.info("********GReg Stratos Apply Tag to Collection Test - Passed ***********");
        } catch (AssertionError e) {
            log.info("Apply Tag to Collection Test Failed :" + e);
            userLogout();
            throw new AssertionError("Apply Tag to Collection Test Failed :" +
                                           e);
        } catch (WebDriverException e) {
            log.info("Apply Tag to Collection Test Failed :" + e);
            userLogout();
            throw new WebDriverException("Apply Tag to Collection Test Failed :" +
                                         e);
        } catch (Exception e) {
            log.info("Apply Tag to Collection Test Failed :" + e);
            userLogout();
            throw new Exception("Apply Tag to Collection Test Failed :" + e);
        }
    }

    @Test(groups = {"wso2.greg"}, description = "apply a lifecycle to a collection", priority = 4)
    public void testAddLifeCycleToCollection() throws Exception {
        String collectionPath = "/selenium_root/collection_root/lifecycle/a1";

        try {
            StratosUserLogin.userLogin(driver, selenium, userName, password, productName);
            gotoDetailViewTab();
            GRegSeleniumUtils.deleteResourceFromBrowser(driver, "selenium_root");
            addCollection(collectionPath);
            assertEquals(collectionPath, selenium.getValue("//input"),
                         "New Created Collection does not Exists :");
            addServiceLifeCycle();
            promoteState();
            waitForLifeCycleStateTransition("Testing");
            promoteState();
            waitForLifeCycleStateTransition("Production");
            //Delete LifeCycle
            deleteServiceLifeCycle();
            userLogout();            //Sign out
            log.info("********GReg Stratos Add Life Cycle to Collection Test - Passed ***********");
        } catch (AssertionError e) {
            log.info("Add Life Cycle to Collection Test Failed :" + e);
            userLogout();
            throw new AssertionError("Add Life Cycle to Collection Test Failed :" +
                                           e);
        } catch (WebDriverException e) {
            log.info("Add Life Cycle to Collection Test Failed :" + e);
            userLogout();
            throw new WebDriverException("Add Life Cycle to Collection Test Failed :" +
                                         e);
        } catch (Exception e) {
            log.info("Add Life Cycle to Collection Test Failed :" + e);
            userLogout();
            throw new Exception("Add Life Cycle to Collection Test Failed :" + e);
        }
    }

    @Test(groups = {"wso2.greg"}, description = "apply a rating to a collection", priority = 5)
    public void testAddRatingToCollection() throws Exception {
        String collectionPath = "/selenium_root/collection_root/rating/a1";

        try {
            StratosUserLogin.userLogin(driver, selenium, userName, password, productName);
            gotoDetailViewTab();
            GRegSeleniumUtils.deleteResourceFromBrowser(driver, "selenium_root");
            addCollection(collectionPath);
            assertEquals(collectionPath, selenium.getValue("//input"),
                         "New Created Collection does not Exists :");
            addRating();
            userLogout();
            log.info("********GReg Stratos Add Rating To Collection Test - Passed ***********");
        } catch (AssertionError e) {
            log.info("Add Rating To Collection Test Failed :" + e);
            userLogout();
            throw new AssertionError("Add Rating To Collection Test Failed :" +
                                           e);
        } catch (WebDriverException e) {
            log.info("Add Rating To Collection Test Failed :" + e);
            userLogout();
            throw new WebDriverException("Add Rating To Collection Test Failed :" +
                                         e);
        } catch (Exception e) {
            log.info("Add Rating To Collection Test Failed :" + e);
            userLogout();
            throw new Exception("Add Rating To Collection Test Failed :" + e);
        }
    }


    @Test(groups = {"wso2.greg"}, description = "rename a collection collection", priority = 6)
    public void testRenameCollection() throws Exception {
        String collectionPath = "/selenium_root/collection_root/rename/a1";
        String rename = "renameda1";

        try {
            StratosUserLogin.userLogin(driver, selenium, userName, password, productName);
            gotoDetailViewTab();
            GRegSeleniumUtils.deleteResourceFromBrowser(driver, "selenium_root");
            addCollection(collectionPath);
            assertEquals(collectionPath, selenium.getValue("//input"),
                         "New Created Collection does not Exists :");
            findLocation("/selenium_root/collection_root/rename");
            assertTrue(selenium.isTextPresent("a1"), "Collection a1 does not Exists :");
            renameCollection(rename);
            userLogout();
            log.info("********GReg Stratos Rename Collection Test - Passed ***********");
        } catch (AssertionError e) {
            log.info("Rename Collection Test Failed :" + e);
            userLogout();
            throw new AssertionError("Rename Collection Test Failed :" +
                                           e);
        } catch (WebDriverException e) {
            log.info("Rename Collection Test Failed :" + e);
            userLogout();
            throw new WebDriverException("Rename Collection Test Failed :" +
                                         e);
        } catch (Exception e) {
            log.info("Rename Collection Test Failed :" + e);
            userLogout();
            throw new Exception("Rename Collection Test Failed :" + e);
        }
    }

    @Test(groups = {"wso2.greg"}, description = "move a collection", priority = 7)
    public void testMoveCollection() throws Exception {
        String collectionPath1 = "/selenium_root/collection_root/move/collection1/a1";
        String collectionPath2 = "/selenium_root/collection_root/move/collection2/b1";
        String movePath = "/selenium_root/collection_root/move/collection2/b1";
        String collection3 = "abc";

        try {
            StratosUserLogin.userLogin(driver, selenium, userName, password, productName);
            gotoDetailViewTab();
            GRegSeleniumUtils.deleteResourceFromBrowser(driver, "selenium_root");
            addCollection(collectionPath1);   //Create Collection  1
            assertEquals(collectionPath1, selenium.getValue("//input"),
                         "New Created Collection1 does not Exists :");
            driver.findElement(By.xpath("//input")).clear();
            findLocation("/");
            addCollection(collectionPath2);          //Create Collection  2
            assertEquals(collectionPath2, selenium.getValue("//input"),
                         "New Created Collection does not Exists :");
            findLocation("/selenium_root/collection_root/move/collection1");
            assertTrue(selenium.isTextPresent("a1"), "a1 Collection does not Exists :");
            moveCollection(movePath);
            findPath(movePath);
            addCollection(collection3);
            findPath(movePath);
            assertTrue(selenium.isTextPresent("a1"), "Moved Collection a1 does not Exists :");
            findPath("/selenium_root/collection_root/move/collection1");
            assertFalse(selenium.isTextPresent("a1"), "a1 Collection has not been moved :");
            userLogout();
            log.info("********GReg Stratos Move a Collection Test - Passed ***********");
        } catch (AssertionError e) {
            log.info("Move a Collection Test Failed :" + e);
            userLogout();
            throw new AssertionError("Move a Collection Test Failed :" +
                                           e);
        } catch (WebDriverException e) {
            log.info("Move a Collection Test Failed :" + e);
            userLogout();
            throw new WebDriverException("Move a Collection Test Failed :" +
                                         e);
        } catch (Exception e) {
            log.info("Move a Collection Test Failed :" + e);
            userLogout();
            throw new Exception("Move a Collection Test Failed :" + e);
        }
    }


    @Test(groups = {"wso2.greg"}, description = "copy a collection", priority = 8)
    public void testCopyCollection() throws Exception {
        String collectionPath1 = "/selenium_root/collection_root/copy/collection1/a1";
        String collectionPath2 = "/selenium_root/collection_root/copy/collection2/b1";
        String collectionPath3 = "/abc";

        try {
            StratosUserLogin.userLogin(driver, selenium, userName, password, productName);
            gotoDetailViewTab();
            GRegSeleniumUtils.deleteResourceFromBrowser(driver, "selenium_root");
            addCollection(collectionPath1);            //Create Collection  1
            assertEquals(collectionPath1, selenium.getValue("//input"),
                         "New Created Collection1 does not Exists :");
            findLocation("/");
            addCollection(collectionPath2);             //Create Collection  2
            assertEquals(collectionPath2, selenium.getValue("//input"),
                         "New Created Collection2 does not Exists :");
            findLocation("/selenium_root/collection_root/copy/collection1");
            copyCollection(collectionPath2);
            findPath(collectionPath2);
            addCollection(collectionPath3); //this is add becoz carbon 3.2.2 has a cashing issue
            findPath(collectionPath2);
            assertTrue(selenium.isTextPresent("a1"), "Copied a1 collection does not Exists :");
            findLocation("/selenium_root/collection_root/copy/collection1");
            assertTrue(selenium.isTextPresent("a1"), "Copied a1 collection does not Exists :");
            userLogout();
            log.info("********GReg Stratos Copy Collection Test - Passed ***********");
        } catch (AssertionError e) {
            log.info("Copy Collection Test Failed :" + e);
            userLogout();
            throw new AssertionError("Copy Collection Test Failed :" +
                                           e);
        } catch (WebDriverException e) {
            log.info("Copy Collection Test Failed :" + e);
            userLogout();
            throw new WebDriverException("Copy Collection Test Failed :" +
                                         e);
        } catch (Exception e) {
            log.info("Copy Collection Test Failed :" + e);
            userLogout();
            throw new Exception("Copy Collection Test Failed :" + e);
        }
    }

    @Test(groups = {"wso2.greg"}, description = "copy a collection", priority = 9)
    public void testDeleteCollection() throws Exception {
        String collectionPath1 = "/selenium_root/collection_root/delete/collection1/a1";
        try {
            StratosUserLogin.userLogin(driver, selenium, userName, password, productName);
            gotoDetailViewTab();
            GRegSeleniumUtils.deleteResourceFromBrowser(driver, "selenium_root");
            addCollection(collectionPath1);               //Create Collection  1
            assertEquals(collectionPath1, selenium.getValue("//input"),
                         "New Created Collection does not Exists :");
            findLocation("/selenium_root/collection_root/delete/");
            deleteCollection();
            userLogout();
            log.info("********GReg Stratos Delete Collection Test - Passed ***********");
        } catch (AssertionError e) {
            log.info("Delete Collection Test  Failed :" + e);
            userLogout();
            throw new AssertionError("Delete Collection Test  Failed :" +
                                           e);
        } catch (WebDriverException e) {
            log.info("Delete Collection Test  Failed :" + e);
            userLogout();
            throw new WebDriverException("Delete Collection Test  Failed :" +
                                         e);
        } catch (Exception e) {
            log.info("Delete Collection Test  Failed :" + e);
            userLogout();
            throw new Exception("Delete Collection Test  Failed :" + e);
        }
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        driver.quit();
    }

    private void deleteCollection() throws InterruptedException {
        driver.findElement(By.id("actionLink1")).click();
        driver.findElement(By.linkText("Delete")).click();
        assertTrue(driver.findElement(By.id("ui-dialog-title-dialog")).getText().contains("WSO2 Carbon"),
                   "Delete Collection pop-up Title does not appear :");
        assertTrue(driver.findElement(By.id("messagebox-confirm")).getText().contains("Are you sure you " +
                                                                                      "want to delete"), "Delete Collection pop-up message" +
                                                                                                         " fail:");
        driver.findElement(By.xpath("//button")).click();
    }

    private void copyCollection(String collectionPath2) throws InterruptedException {
        driver.findElement(By.id("actionLink1")).click();
        driver.findElement(By.linkText("Copy")).click();
        driver.findElement(By.id("copy_destination_path1")).sendKeys(collectionPath2);
        //click on copy button
        driver.findElement(By.xpath("//td/table/tbody/tr[2]/td/input")).click();
        assertTrue(driver.findElement(By.id("ui-dialog-title-dialog")).getText().contains("WSO2 Carbon"));
        assertTrue(driver.getPageSource().contains("WSO2 Carbon"),
                   "Copy Collection pop-up Title Failed:");
        assertTrue(driver.getPageSource().contains("Successfully copied collection."),
                   "Copy Collection pop-up Message Failed :");
        driver.findElement(By.xpath("//button")).click();
    }

    private void moveCollection(String movePath) throws InterruptedException {
        driver.findElement(By.id("actionLink1")).click();
        driver.findElement(By.linkText("Move")).click();
        driver.findElement(By.id("move_destination_path1")).sendKeys(movePath);
        driver.findElement(By.xpath("//tr[5]/td/table/tbody/tr[2]/td/input")).click();
        assertTrue(driver.findElement(By.id("ui-dialog-title-dialog")).getText().contains("WSO2 Carbon"));
        assertTrue(driver.getPageSource().contains("WSO2 Carbon"), "Move pop-up window Title fail");
        assertTrue(driver.getPageSource().contains("Successfully moved collection."),
                   "Move pop-up window message fail");
        driver.findElement(By.xpath("//button")).click();
    }

    private void renameCollection(String rename) throws InterruptedException {
        driver.findElement(By.id("actionLink1")).click();
        //waitTime("//tr[2]/td/div/a");
        driver.findElement(By.linkText("Rename")).click();
        //waitTime("//tr[6]/td/table/tbody/tr/td/input");
        driver.findElement(By.id("resourceEdit1")).clear();
        driver.findElement(By.id("resourceEdit1")).sendKeys(rename);
        driver.findElement(By.xpath("//tr[6]/td/table/tbody/tr[2]/td/input")).click();
        assertTrue(driver.findElement(By.id("ui-dialog-title-dialog")).getText().contains("WSO2 Carbon"));
        assertTrue(driver.getPageSource().contains("WSO2 Carbon"), "rename pop-up title fail :");
        driver.findElement(By.xpath("//button")).click();
        assertTrue(driver.getPageSource().contains(rename), "Renamed Collection does not Exists :");
    }

    private void findPath(String path) throws InterruptedException {
        driver.findElement(By.id("uLocationBar")).clear();
        driver.findElement(By.id("uLocationBar")).sendKeys(path);
        driver.findElement(By.xpath("//input[2]")).click();
    }


    private void findLocation(String path) throws Exception {
        driver.findElement(By.id("uLocationBar")).clear();
        driver.findElement(By.id("uLocationBar")).sendKeys(path);
        driver.findElement(By.xpath("//input[2]")).click();
    }

    private void addRating() throws InterruptedException {
        // Add rating 1
        driver.findElement(By.xpath("//img[3]")).click();
        assertTrue(selenium.isTextPresent("(1.0)"), "Rating 1 has failed :");
        // Add rating 2
        driver.findElement(By.xpath("//img[5]")).click();
        assertTrue(selenium.isTextPresent("(2.0)"), "Rating 2 has failed :");
        // Add rating 3
        driver.findElement(By.xpath("//img[7]")).click();
        assertTrue(selenium.isTextPresent("(3.0)"), "Rating 3 has failed :");
        // Add rating 4
        driver.findElement(By.xpath("//img[9]")).click();
        assertTrue(selenium.isTextPresent("(4.0)"), "Rating 4 has failed :");
        // Add rating 5
        driver.findElement(By.xpath("//img[11]")).click();
        assertTrue(selenium.isTextPresent("(5.0)"), "Rating 5 has failed :");
    }

    private void deleteServiceLifeCycle() throws InterruptedException {
        driver.findElement(By.xpath("//div[3]/div[2]/table/tbody/tr/td/div/a")).click();
        //waitTime("//body/div[3]/div/div");
        assertTrue(driver.getPageSource().contains("WSO2 Carbon"), "Delete Lifecycle pop-up Title fail:");
        assertTrue(driver.findElement(By.id("messagebox-confirm")).getText().contains("Are you sure you want to delete"),
                   "Delete Lifecycle pop-up message fail :");
        driver.findElement(By.xpath("//button")).click();
    }

    private void addServiceLifeCycle() throws InterruptedException {
        driver.findElement(By.id("lifecycleIconMinimized")).click();
        driver.findElement(By.linkText("Add Lifecycle")).click();
        assertTrue(driver.getPageSource().contains("Enable Lifecycle"),
                   "Enable LifeCycle Text does not appear :");
        assertEquals("Add", selenium.getValue("//div[3]/div[3]/form/table/tbody/tr[2]/td/input"),
                     "Lifecycle Add Button does not appear :");
        assertEquals("Cancel", selenium.getValue("//div[3]/div[3]/form/table/tbody/tr[2]/td/input[2]"),
                     "Lifecycle Cancel button does not appear");
        driver.findElement(By.xpath("//div[3]/div[3]/form/table/tbody/tr[2]/td/input")).click();
        assertTrue(driver.findElement(By.id("aspectList")).getText().contains("Development"));
        assertTrue(driver.getPageSource().contains("Development"),
                   "Service Life cycle default state does not exists:");
    }

    private void deleteTag() throws InterruptedException {
        driver.findElement(By.xpath("//a[2]/img")).click();
        assertTrue(driver.findElement(By.id("ui-dialog-title-dialog")).getText().contains("WSO2 Carbon"),
                   "Popup not found :");
        driver.findElement(By.xpath("//button")).click();
    }

    private void addTag(String tag) throws InterruptedException {
        driver.findElement(By.id("tagsIconMinimized")).click();
        driver.findElement(By.linkText("Add New Tag")).click();
        driver.findElement(By.id("tfTag")).sendKeys(tag);
        driver.findElement(By.xpath("//div[2]/input[3]")).click();
    }

    private void userLogout() throws InterruptedException {
        driver.findElement(By.className("right-links")).findElement(By.linkText("Sign-out")).click();
    }


    private void addCollection(String collectionPath) throws Exception {
        driver.findElement(By.linkText("Add Collection")).click();
        driver.findElement(By.id("collectionName")).sendKeys(collectionPath);
        driver.findElement(By.id("colDesc")).sendKeys("Selenium Test");
        driver.findElement(By.xpath("//div[7]/form/table/tbody/tr[5]/td/input")).click();
        assertTrue(driver.findElement(By.id("ui-dialog-title-dialog")).getText().contains("WSO2 Carbon"));
        assertTrue(driver.getPageSource().contains("WSO2 Carbon"),
                   "Add new Collection pop -up title failed :");
        assertTrue(driver.getPageSource().contains("Successfully added new collection."),
                   "Add new Collection pop -up message failed :");
        driver.findElement(By.xpath("//div[2]/button")).click();
    }

    private void deleteComment() throws InterruptedException {
        driver.findElement(By.id("closeC0")).click();
        assertTrue(driver.findElement(By.id("ui-dialog-title-dialog")).getText().contains("WSO2 Carbon"),
                   "Popup not found :");
        driver.findElement(By.xpath("//button")).click();
    }

    private void addComment(String comment) throws InterruptedException {
        if (!driver.findElement(By.id("commentsIconExpanded")).isDisplayed()) {
            driver.findElement(By.id("commentsIconMinimized")).click();
        }
        driver.findElement(By.linkText("Add Comment")).click();
        assertTrue(selenium.isTextPresent("Add New Comment"),
                   "Add comment window pop -up title failed :");

        driver.findElement(By.id("comment")).sendKeys(comment);
        driver.findElement(By.xpath("//td[4]/div[12]/div/div[3]/div[3]/form/table/tbody/tr[2]/td/input")).click();
    }

    private void gotoDetailViewTab() throws Exception {
        driver.findElement(By.linkText("Browse")).click();    //Click on Browse link
        GRegSeleniumUtils.waitForBrowserPage(driver);
        driver.findElement(By.id("stdView")).click();        //Go to Detail view Tab
        assertTrue(selenium.isTextPresent("Browse"), "Browse Detail View Page fail :");
        assertTrue(selenium.isTextPresent("Metadata"), "Browse Detail View Page fail Metadata:");
    }

    private void applyTag(String tagName) throws InterruptedException {

        if (!driver.findElement(By.id("tagAddDiv")).isDisplayed()) {
            driver.findElement(By.id("tagsIconMinimized")).click();
        }

        driver.findElement(By.linkText("Add New Tag")).click();//click on Add New Tag
        GRegSeleniumUtils.waitForElement(driver, "id", "tfTag");
        driver.findElement(By.id("tfTag")).sendKeys(tagName);
        driver.findElement(By.xpath("//div[2]/input[3]")).click();
        GRegSeleniumUtils.waitForElement(driver, "xpath",
                                         "//tr/td[4]/div[12]/div/div[12]/div[3]/a");
    }

    private void promoteState() throws Exception {
        GRegSeleniumUtils.waitForElement(driver, "id", "option0");
        GRegSeleniumUtils.waitForElement(driver, "id", "option1");
        GRegSeleniumUtils.waitForElement(driver, "id", "option2");

        driver.findElement(By.id("option0")).click();
        waitForLifeCycleCheck("option0");
        driver.findElement(By.id("option1")).click();
        waitForLifeCycleCheck("option1");
        Thread.sleep(5000);
        driver.findElement(By.id("option2")).click();
        waitForLifeCycleCheck("option2");

        driver.findElement(By.xpath("//tr/td[4]/div[11]/div[3]/div[2]/table/tbody/tr[2]/td/div" +
                                    "/input")).click();

        assertTrue(driver.findElement(By.id("ui-dialog-title-dialog")).getText().contains("WSO2 Carbon"),
                   "Popup not found :");
        driver.findElement(By.xpath("//button")).click();
    }

    private boolean waitForLifeCycleCheck(String optionId) {
        long currentTime = System.currentTimeMillis();
        long exceededTime;
        do {
            try {
                if (driver.findElement(By.id(optionId)).getAttribute("checked") != null) {
                    return true;
                }
            } catch (WebDriverException ignored) {
                log.info("Waiting for LC checklist options");
            }
            exceededTime = System.currentTimeMillis();

        } while (!(((exceededTime - currentTime) / 1000) > 60));
        return false;
    }

    private void waitForLifeCycleStateTransition(String state) {
        long currentTime = System.currentTimeMillis();
        long exceededTime;
        Boolean status = false;
        do {
            try {
                if (driver.findElement(By.xpath("//div[11]/div[3]/div[2]/table/tbody/tr/td/div[2]" +
                                                "/table/tbody/tr[2]/td")).getText().contains(state)) {
                    status = true;
                    break;
                }
            } catch (WebDriverException ignored) {
                log.info("Waiting for LC checklist options");
            }
            exceededTime = System.currentTimeMillis();

        } while (!(((exceededTime - currentTime) / 1000) > 30));
        assertTrue(status, "LifeCycle state not found - " + state);
    }

}

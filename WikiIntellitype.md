# Details #

Step-by-step instructions on how to set JIntellitype in Development Environment:

  1. Update your code to the latest version. You should see a new folder lib.
  1. Add the jintellitype-1.3.7 file into your build path.
  1. Right-click on the new intellitype-1.3.7 icon under your **referenced libraries** and click **properties**
  1. Click on the **Native Library** tab, type Fin/lib (or click workspace, and look for the folder with all the libraries there. Essentially I placed the JIntellitype dll there and it needs to look for the dll in the right place)
  1. Click on the **Javadoc Location** tab, click the **Javadoc in archive** radio button, and look for the same lib folder and link the javadocs.
  1. You should be set to use JIntellitype code in your future builds! I've some test files in the test folder in cs2103.aug11.t11j2.fin.gui.listener.
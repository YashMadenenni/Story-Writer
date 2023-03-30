package cs5031.shared_communication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cs5031.shared_communication.cli.CommandLine;

@SpringBootApplication
public class BackendApplication {

	/**Main class to invoke backend 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
		CommandLine commandLineStart = new CommandLine();
	}

}

package awsInstance.createECInstance;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceNetworkInterfaceSpecification;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.Tag;

public class App 
{
    public static void main( String[] args )
    {
    	   final AWSCredentials AWS_CREDENTIALS;
    	    
    	        // Your accesskey and secretkey
    	        AWS_CREDENTIALS = new BasicAWSCredentials(
    	                "AKIAXSP7KE2NUBBQA5UP",
    	                "uXSJrZ28yR+dcZ/448rj5yercyWeNTUuK2kQ4i9J"
    	        );
    	
    	// Set up the amazon ec2 client
        AmazonEC2 ec2Client = AmazonEC2ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(AWS_CREDENTIALS))
                .withRegion(Regions.US_EAST_1)
                .build();
         
        // Launch an Amazon EC2 Instance
        RunInstancesRequest runInstancesRequest = new RunInstancesRequest().withImageId("ami-777777")
                .withInstanceType("t2.micro") // https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/instance-types.html
                .withMinCount(1)
                .withMaxCount(1)
                .withNetworkInterfaces(new InstanceNetworkInterfaceSpecification()
                        .withAssociatePublicIpAddress(true)
                        .withDeviceIndex(0)
                        .withSubnetId("subnet-777777")
                        .withGroups("sg-777777"));
 
        RunInstancesResult runInstancesResult = ec2Client.runInstances(runInstancesRequest);
 
        Instance instance = runInstancesResult.getReservation().getInstances().get(0);
        String instanceId = instance.getInstanceId();
        System.out.println("EC2 Instance Id: " + instanceId);
 
        // Setting up the tags for the instance
        CreateTagsRequest createTagsRequest = new CreateTagsRequest()
                .withResources(instance.getInstanceId())
                .withTags(new Tag("Name", "codeflex-ec2"));
        ec2Client.createTags(createTagsRequest);
 
        // Starting the Instance
        StartInstancesRequest startInstancesRequest = new StartInstancesRequest().withInstanceIds(instanceId);
 
        ec2Client.startInstances(startInstancesRequest);

    	
        
    }

	
}

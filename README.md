### Instructions


#### write your local ip for each step

1) **Generate certificates:**
    - you can use git bash
    - write your local ip address of your computer/host like `192.168.0.3` (Linux terminal command: `hostname -I`)
    - please create an empty ssl folder under the project directory


`mkdir ssl && openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout ssl/private_key.pem -out ssl/certificate.pem -subj "//C=US//ST=California//L=San Francisco//O=MyOrganization//OU=MyDepartment//CN=<YOUR_LOCAL_IP>"`

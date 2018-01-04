## Vagrant virtual machine for clic.xtec.cat

### Requirements

* You need to have [Vagrant](https://www.vagrantup.com/) and [VirtualBox](https://www.virtualbox.org) installed.
* The [vagrant-hosts](https://github.com/oscar-stack/vagrant-hosts) vagrant plugin is also required:

    `vagrant plugin install vagrant-hosts`


### Vagrant commands

* **`vagrant up`** to start the VM.

* **`vagrant ssh`** to enter into the VM via ssh.

* **`vagrant halt`** to stop the VM.

* **`vagrant reload`** to restart the VM.

* **`vagrant destroy`** to completely delete the VM. Asks for confirmation.

### Hostnames and IP

You must add this entry to your `/etc/hosts` to access the VM:

`192.168.33.2 clic-virtual.xtec.cat`

### Accessing the virtual machine

Once started, the VM pages are available at:

http://clic-virtual.xtec.cat
https://clic-virtual.xtec.cat (fake SSL certificate must be accepted)

### Useful resources

* [Vagrant](http://www.vagrantup.com/)
* [VirtualBox](https://www.virtualbox.org/)
* [vbguest](https://github.com/dotless-de/vagrant-vbguest)
* [enable virtualization](http://www.sysprobs.com/disable-enable-virtualization-technology-bios)
* [vagrant-hosts](https://github.com/adrienthebo/vagrant-hosts)


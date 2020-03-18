
/*
**  CS152 - Assignment 5
**  Mingyun Kim
*/
object GladiatorLab extends App {

	class Gladiator(val name: String) {
		protected var health: Int = 100

		def damage(amount: Int): Unit = {
			if (health < amount) health = 0
			else health -= amount
			println(this.name + "'s health: " + (this.health + amount) + " -> " +
			this.health)
		}

		def attack(taget: Gladiator): Unit = {
			if (this.health > 0) {
				println(this.name + " is attacking " + taget.name)
				taget.damage(scala.util.Random.nextInt(this.health))
			}
			else println("Dead gladiators don't get fight!")
		}
	}

	trait Slasher {
		def slash(taget: Gladiator): Unit = {
			println("Slash!")
			taget.damage(5)
		}
	}

	trait Masher {
		def mash(taget: Gladiator): Unit = {
			println("Mash!")
			taget.damage(5)
		}
	}

	trait Crusher {
		def crush(taget: Gladiator): Unit = {
			println("Crush!")
			taget.damage(5)
		}
	}

	class CrusherMasher(name: String) extends Gladiator(name) with Crusher with Masher {
		override def attack(taget: Gladiator): Unit = {
			if (health > 0) {
				println(this.name + " is attacking " + taget.name)
				crush(taget)
				mash(taget)
			} else println("Dead gladiators don't fight!")
		}
	}

	var optimus = new CrusherMasher("Optimus Prime")
	var bee = new Gladiator("Bumble Bee") with Slasher with Masher {
		override def attack(taget: Gladiator): Unit = {
			if (health > 0) {
				println(this.name + " is attacking " + taget.name)
				slash(taget)
				mash(taget)
			} else println("Dead gladiators don't fight!")
		}
	}
	for (i <- 0 to 5) {
		optimus.attack(bee)
		bee.attack(optimus)
	}
}

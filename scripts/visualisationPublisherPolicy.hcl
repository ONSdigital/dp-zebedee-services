path "transit/keys/collection1" {
	policy = "read"
}

path "transit/decrypt/collection1" {
	policy = "write"
}

path "transit/encrypt/collection1" {
	policy = "write"
}

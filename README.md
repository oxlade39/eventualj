eventualj
====================

Eventual assertions

Examples
---------------------

`assertThat(eventually(ten()).getValue(), willBe(10)); // passes`
`assertThat(ten().getValue(), willBe(10)); // fails`
`assertThat(eventually(messageQueue).isEmpty(), willBe(true)); // passes eventually`
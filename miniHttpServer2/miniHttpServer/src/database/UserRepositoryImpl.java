package database;

import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository{
	private List<User> users;
	
	public UserRepositoryImpl() {
		this.users=new ArrayList();
	}

	@Override
	public void save(User user) {
		this.users.add(user);
		
	}

	@Override
	public List<User> findAll() {
		return this.users;
	}

	@Override
	public Long count() {
		return (long) this.users.size();
	}

	@Override
	public void delete(String id) {
		this.users.removeIf(user -> user.getId().equals(id));
		
	}

	@Override
	public boolean exists(String id) {
		return this.users.stream()
			    .anyMatch(user -> user.getId().equals(id));
	}

	@Override
	public User findOne(String id) {
		return this.users.stream()
        .filter(u -> u.getId().equals(id))
        .findFirst()
        .orElse(null);
	}

}
